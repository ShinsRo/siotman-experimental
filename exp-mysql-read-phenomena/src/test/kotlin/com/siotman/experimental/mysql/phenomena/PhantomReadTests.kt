package com.siotman.experimental.mysql.phenomena

import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.dbUrl
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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class PhantomReadTests : ReadPhenomenaTests({
    "RU, RC 격리수준에서 PhantomRead 를 확인할 수 있다" {
        forAll(
            table(
                headers("신스로의 격리수준", "카리나의 격리수준"),
                row(Connection.TRANSACTION_READ_UNCOMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_READ_COMMITTED, Connection.TRANSACTION_READ_COMMITTED),
                row(Connection.TRANSACTION_REPEATABLE_READ, Connection.TRANSACTION_READ_COMMITTED)
            )
        ) { shinsRoIsolation, karinaIsolation ->
            println("신스로의 격리수준이 ${ReadPhenomenaTestUtils.connectionValueOf(shinsRoIsolation)} 일 때는 아래와 같습니다.")

            transaction {
                Employees.replace { row ->
                    row[Employees.id] = 2
                    row[fullName] = "ShinsRo 1"
                    row[daysOff] = 2
                }
                Employees.replace { row ->
                    row[Employees.id] = 3
                    row[fullName] = "ShinsRo 2"
                    row[daysOff] = 5
                }
                Employees.replace { row ->
                    row[Employees.id] = 4
                    row[fullName] = "ShinsRo 3"
                    row[daysOff] = 5
                }
            }

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

            // 1. 신스로가 휴가를 3번 이상 사용한 직원을 검색
            shinsRoInbound.send(TxScenario { username ->
                val employees = Employees.findAllByDaysOffGte(3)

                employees.count() shouldBeExactly 2
                println("[$username] 휴가를 3 번 이상 사용한 직원 수는 ${employees.count()} 입니다.")
            })

            // 2. 카리나가 휴가를 5번 사용한 "ShinsRo 2" 를 삭제
            karinaInbound.send(TxScenario { username ->
                Employees.deleteWhere { fullName eq "ShinsRo 2" }
                println("[$username] \"ShinsRo 2\" 를 삭제했습니다.")
            })

            karinaInbound.send(TxScenario(isEnd = true) { commit() })
            karinaInbound.close()

            // 3. 신스로가 휴가를 3번 이상 사용한 직원을 검색
            shinsRoInbound.send(TxScenario { username ->
                val employees = Employees.findAllByDaysOffGte(3)

                employees.count() shouldBeExactly when (shinsRoIsolation) {
                    Connection.TRANSACTION_READ_UNCOMMITTED -> 1
                    Connection.TRANSACTION_READ_COMMITTED -> 1

                    // 위키와는 다른 부분입니다.
                    // MySQL 에서 REPEATABLE_READ 는 팬텀리드를 회피합니다!
                    // (참고로 Phantom Write 현상은 존재합니다.)
                    Connection.TRANSACTION_REPEATABLE_READ -> 2

                    else -> throw NotImplementedError()
                }
                println("[$username] 휴가를 3 번 이상 사용한 직원 수는 ${employees.count()} 입니다.")
            })

            shinsRoInbound.send(TxScenario(isEnd = true) { commit() })
            shinsRoInbound.close()

        }
    }
})