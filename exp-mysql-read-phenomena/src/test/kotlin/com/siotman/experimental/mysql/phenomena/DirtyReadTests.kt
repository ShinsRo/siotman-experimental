package com.siotman.experimental.mysql.phenomena

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

private object DirtyReadEmployeeTable : IntIdTable() {
    val fullName = varchar("full_name", 30)
    val offDays = integer("off_days")
}

private typealias Employees = DirtyReadEmployeeTable

class DirtyReadTests : StringSpec({
    // Database 설정
    // 두 세션, shinsro 의 세션과 karina 의 세션이 존재함을 가정한다.
    val dbUrl = "jdbc:mysql://localhost:3306/read_phenomena_test"
    val dbFirstUsername = "shinsro"
    val dbSecondUsername = "karina"

    val dbSessions = mapOf(
        dbFirstUsername to Database.connect(url = dbUrl, user = dbFirstUsername),
        dbSecondUsername to Database.connect(url = dbUrl, user = dbSecondUsername)
    )

    // 직원 신스로 정보 초기화 함수
    val shinsRoFullname = "Shins Ro"
    fun Transaction.initShinsRo() {
        Employees.replace { employees ->
            employees[id] = 1
            employees[fullName] = shinsRoFullname
            employees[offDays] = 0
        }
    }

    // 테스트 시나리오 제어를 위한 코루틴 인바운드 이벤트 채널 정의
    val coroutineInBoundChannels = mapOf(
        // DB 유저 이름 to 실행할 함수
        dbFirstUsername to Channel<suspend Transaction.() -> Any?>(),
        dbSecondUsername to Channel<suspend Transaction.() -> Any?>()
    )


    // 이벤트 행위 1: 신스로의 휴가일수 조회
    fun Transaction.selectShinsRoOffDays() {
        val statement = Employees.select(where = { Employees.fullName eq shinsRoFullname })

        println("$shinsRoFullname 의 휴가일수는 ${statement.single()[Employees.id].value} 입니다.")
    }

    // 이벤트 행위 2: 신스로의 휴가일수 + 1
    fun Transaction.increaseShinsRoOffDaysOne() {
        Employees.update(where = { Employees.fullName eq shinsRoFullname }) {
            it.update(Employees.offDays, Employees.offDays + 1)
        }

        println("$shinsRoFullname 의 휴가일수를 +1 했습니다.")
    }

    // 이벤트 행위 3: close Tx
    fun Transaction.commitAndClose() {
        commit()
        close()
    }

    //
    suspend fun receiveEventsUntilClose(username: String, isolation: Int) {
        val session = dbSessions[username]
        val inbound = coroutineInBoundChannels[username]!!

        newSuspendedTransaction(db = session, transactionIsolation = isolation) {
            do {
                val event = inbound.receive()
                event.invoke(this)
            } while (event == Transaction::commitAndClose)
        }
    }

    // === 테스트 케이스 시작 ===
    "Dirty Reads 는 READ_UNCOMMITTED 격리수준에서 발생한다" {
        transaction(statement = Transaction::initShinsRo)

        val shinsRoJob = launch {
            receiveEventsUntilClose(dbFirstUsername, Connection.TRANSACTION_READ_COMMITTED)
        }

        val karinaJob = launch {
            receiveEventsUntilClose(dbFirstUsername, Connection.TRANSACTION_SERIALIZABLE)
        }

        val shinsRoInbound = coroutineInBoundChannels[dbFirstUsername]!!
        val karinaRoInbound = coroutineInBoundChannels[dbSecondUsername]!!

        TODO("시나리오 이벤트 호출")

        shinsRoJob.join()
        karinaJob.join()
    }

    "Dirty Reads 는 READ_UNCOMMITTED 외 격리수준에서 발생하지 않는다" {
        TODO()
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