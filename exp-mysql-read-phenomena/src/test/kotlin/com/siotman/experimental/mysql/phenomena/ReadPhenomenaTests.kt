package com.siotman.experimental.mysql.phenomena

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

abstract class ReadPhenomenaTests(body: StringSpec.() -> Unit = {}) : StringSpec(body) {
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
}