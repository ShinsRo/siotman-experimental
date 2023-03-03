package com.siotman.experimental.mysql.phenomena

import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.connectionValueOf
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.dbUrl
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.shinsRoFullname
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.userForKarina
import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.userForShins
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.ints.shouldBeExactly
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class NonRepeatableReadTests : ReadPhenomenaTests({

    "READ_UNCOMMITTED, READ_COMMITTED 에서는 NonRepeatableReads 현상이 발생한다" {
        forAll(
            table(
                headers("신스로의 격리수준", "카리나의 격리수준"),
                row(Connection.TRANSACTION_READ_UNCOMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_READ_COMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_REPEATABLE_READ, Connection.TRANSACTION_READ_COMMITTED)
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

            // 2. 카리나가 신스로의 휴가일수를 +1 하고 커밋
            karinaInbound.send(TxScenario(isEnd = true) { username ->
                val daysOff = Employees.increaseDaysOff(shinsRoFullname, 1)
                commit()

                daysOff shouldBeExactly 1
                println("[$username] $shinsRoFullname 의 휴가일수를 +1 했습니다.")
            })

            karinaInbound.close()
            karinaJob.join()

            // 3. 신스로가 신스로의 휴가일수를 조회
            shinsRoInbound.send(TxScenario { username ->
                val daysOff = Employees.findDaysOff(shinsRoFullname)

                daysOff shouldBeExactly when (shinsRoIsolation) {
                    Connection.TRANSACTION_READ_UNCOMMITTED -> 1
                    Connection.TRANSACTION_READ_COMMITTED -> 1
                    Connection.TRANSACTION_REPEATABLE_READ -> 0

                    else -> throw NotImplementedError()
                }
                println("[$username] $shinsRoFullname 의 휴가일수는 $daysOff 입니다.")
            })

            shinsRoInbound.send(TxScenario(isEnd = true) { commit() })
            shinsRoInbound.close()

            shinsRoJob.join()
        }
    }
})