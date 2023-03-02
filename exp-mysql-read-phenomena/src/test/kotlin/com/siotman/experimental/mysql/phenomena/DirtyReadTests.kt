package com.siotman.experimental.mysql.phenomena

import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.connectionValueOf
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.dbUrl
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.shinsRoFullname
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.userForKarina
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.userForShins
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.ints.shouldBeExactly
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DirtyReadTests : StringSpec({

    "Dirty Reads 는 READ_UNCOMMITTED 격리수준에서만 발생한다" {
        forAll(
            table(
                headers("신스로의 격리수준", "카리나의 격리수준"),
                row(Connection.TRANSACTION_READ_UNCOMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_READ_COMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_REPEATABLE_READ, Connection.TRANSACTION_READ_COMMITTED),

//                InnoDB 의 경우, SERIALIZABLE 에서 모든 select 가 select ... for share 로 치환됩니다.
//                해서 위키의 시나리오가 MySQL innoDB 에서는 어플리케이션 데드락을 발생시키는 바, 결과를 눈으로 확인할 순 없습니다.
//                row(Connection.TRANSACTION_SERIALIZABLE, Connection.TRANSACTION_READ_UNCOMMITTED)
            )
        ) { shinsRoIsolation, karinaIsolation ->
            println("신스로의 격리수준이 ${connectionValueOf(shinsRoIsolation)} 일 때는 아래와 같습니다.")

            transaction(statement = Transaction::initShinsRo)

            val shinsRoInbound = Channel<TxScenario>()
            val karinaInbound = Channel<TxScenario>()

            val shinsRoJob = launch {
                receiveTxScenarios(
                    txIdentifier = userForShins,
                    isolation = shinsRoIsolation,
                    dbSession = Database.connect(url = dbUrl, user = userForShins),
                    channel = shinsRoInbound
                )
            }

            val karinaJob = launch {
                receiveTxScenarios(
                    txIdentifier = userForKarina,
                    isolation = karinaIsolation,
                    dbSession = Database.connect(url = dbUrl, user = userForKarina),
                    channel = karinaInbound
                )
            }

            // 1. 신스로가 신스로의 휴가일수를 조회
            shinsRoInbound.send(TxScenario { username ->
                val daysOff = Employees.findDaysOff(shinsRoFullname)

                daysOff shouldBeExactly 0
                println("[$username] $shinsRoFullname 의 휴가일수는 $daysOff 입니다.")
            })

            // 2. 카리나가 신스로의 휴가일수를 +1
            karinaInbound.send(TxScenario { username ->
                val daysOff = Employees.increaseDaysOff(shinsRoFullname, 1)

                daysOff shouldBeExactly 1
                println("[$username] $shinsRoFullname 의 휴가일수를 +1 했습니다.")
            })

            // 3. 신스로가 신스로의 휴가일수를 조회
            shinsRoInbound.send(TxScenario { username ->
                val daysOff = Employees.findDaysOff(shinsRoFullname)

                daysOff shouldBeExactly when (shinsRoIsolation) {
                    Connection.TRANSACTION_READ_UNCOMMITTED -> 1
                    Connection.TRANSACTION_READ_COMMITTED -> 0
                    Connection.TRANSACTION_REPEATABLE_READ -> 0

                    else -> throw NotImplementedError()
                }
                println("[$username] $shinsRoFullname 의 휴가일수는 $daysOff 입니다.")
            })

            shinsRoInbound.send(TxScenario(isEnd = true) { commit() })
            karinaInbound.send(TxScenario(isEnd = true) { commit() })

            shinsRoInbound.close()
            karinaInbound.close()

            shinsRoJob.join()
            karinaJob.join()
        }
    }
}) {
    override suspend fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        // 디비 Configuration
        TransactionManager.defaultDatabase = Database.connect(
            url = "jdbc:mysql://localhost:3306/read_phenomena_test",
            user = "root"
        )

        // 테스트용 DLL 실행
        transaction {
            SchemaUtils.create(Employees)
        }
    }

    override suspend fun afterSpec(spec: Spec) {
        super.afterSpec(spec)

        // 테스트용 DLL 드롭
        transaction {
            SchemaUtils.drop(Employees)
        }
    }
}