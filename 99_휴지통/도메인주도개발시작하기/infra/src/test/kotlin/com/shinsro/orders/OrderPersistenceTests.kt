@file:Suppress("NonAsciiCharacters")

package com.shinsro.orders

import com.shinsro.customers.Customer
import com.shinsro.customers.CustomerId
import com.shinsro.customers.CustomerName
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.TestConstructor
import kotlin.test.assertNotNull

@DataJpaTest
@ComponentScan("com.shinsro.orders")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderPersistenceTests(private val persistence: OrderPersistence) {

    @Test
    fun `주문 도메인을 엔티티로 영속하면, 주문번호로 주문 도메인을 조회할 수 있다`() {
        val orderNo = OrderNo("ORDER-0001")
        val orderer = Customer(
            id = CustomerId("CUSTOMER-0001"),
            customerName = CustomerName("신스로"),
        )

        persistence.save(Order(no = orderNo, orderer = orderer))

        with(persistence.findByIdOrNull(orderNo)) {
            assertNotNull(this)

            no shouldBe OrderNo("ORDER-0001")
            orderer.id shouldBe "CUSTOMER-0001"
        }
    }
}
