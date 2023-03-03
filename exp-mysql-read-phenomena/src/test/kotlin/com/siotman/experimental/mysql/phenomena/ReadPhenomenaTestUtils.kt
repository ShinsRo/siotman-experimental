package com.siotman.experimental.mysql.phenomena

import com.siotman.experimental.mysql.phenomena.ReadPhenomenaTestUtils.shinsRoFullname
import kotlinx.coroutines.channels.Channel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection

object ReadPhenomenaTestUtils {
    // Database 설정
    // 두 세션, shinsro 의 세션과 karina 의 세션이 존재함을 가정한다.
    const val dbUrl = "jdbc:mysql://localhost:3306/read_phenomena_test"
    const val userForShins = "shinsro"
    const val userForKarina = "karina"

    // 테스트 직원 신스로 풀네임
    const val shinsRoFullname = "Shins Ro"

    fun connectionValueOf(intValue: Int) = when (intValue) {
        Connection.TRANSACTION_READ_UNCOMMITTED -> "READ_UNCOMMITTED"
        Connection.TRANSACTION_READ_COMMITTED -> "READ_COMMITTED"
        Connection.TRANSACTION_REPEATABLE_READ -> "REPEATABLE_READ"
        Connection.TRANSACTION_SERIALIZABLE -> "SERIALIZABLE"
        else -> throw Exception("다루지 않거나 정의하지 않은 격리수준입니다.")
    }
}


// 직원 신스로 정보 초기화 함수
fun Transaction.initShinsRo() {
    Employees.replace { employees ->
        employees[Employees.id] = 1
        employees[fullName] = shinsRoFullname
        employees[daysOff] = 0
    }
}

// 트랜잭션 테스트 시나리오 제어를 위한 코루틴 채널과 채널메세지 클래스
class TxScenario(
    val isEnd: Boolean = false,
    val statement: suspend Transaction.(username: String) -> Unit
)

// 시나리오 채널 서빙 시작함수
suspend fun receiveTxScenarios(
    txIdentifier: String,
    isolation: Int,
    dbSession: Database,
    channel: Channel<TxScenario>
) = newSuspendedTransaction(
    db = dbSession,
    transactionIsolation = isolation
) {
    do {
        val scenario = channel.receive()
        scenario.statement.invoke(this, txIdentifier)
    } while (!scenario.isEnd)
}
