package com.siotman.experimental.mysql.phenomena

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class Employee(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Employee>(Employees)

    val fullName by Employees.fullName
    var daysOff by Employees.daysOff
}

object Employees : IntIdTable() {
    val fullName = varchar("full_name", 30)
    val daysOff = integer("days_off")

    // 유저의 사용 휴가 일수 조회
    fun findDaysOff(fullName: String): Int = select {
        Employees.fullName eq fullName
    }.single()[daysOff]

    // 사용 휴가 일수 + 1
    fun increaseDaysOff(fullName: String, amount: Int) = update(where = {
        Employees.fullName eq fullName
    }) {
        it.update(daysOff, daysOff + amount)
    }

    // 사용 휴가 일수가 주어진 daysOff 이상인 Employees
    fun findAllByDaysOffGte(daysOff: Int) = select {
        Employees.daysOff greaterEq daysOff
    }.map { Employee.wrapRow(it) }
}

