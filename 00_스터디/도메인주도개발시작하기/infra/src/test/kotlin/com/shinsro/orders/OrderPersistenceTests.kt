@file:Suppress("NonAsciiCharacters")

package com.shinsro.orders

import com.shinsro.customers.Customer
import com.shinsro.customers.CustomerId
import com.shinsro.customers.CustomerName
import com.shinsro.privacies.Address
import com.shinsro.privacies.PersonName
import com.shinsro.privacies.ZipCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.TestConstructor

@DataJpaTest
@ComponentScan("com.shinsro.orders")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderPersistenceTests(private val persistence: OrderPersistence) {

    @Test
    fun `주문 엔티티 CRUD - 임시 테스트`() {
        /**
         * 1. Value Object 를 일일이 생성
         * 2. 도메인 엔티티와 JPA 엔티티 분리
         *
         * 이 두가지가 합쳐졌을 때, 작은 구현임에도 불구하고 코드량이 많다.
         * 어디까지 타협하는 것이 옳은가. 이에 대한 고민과 나의 답이 필요하다.
         */

        val orderNo = OrderNo("ORDER-0001")

        persistence.save(
            Order(
                no = orderNo,

                orderer = Customer(
                    id = CustomerId("CUSTOMER-0001"),
                    customerName = CustomerName(""),
                ),

                shippingInfo = ShippingInfo(
                    recipientName = PersonName(""),
                    address = Address(
                        base = "",
                        detail = "",
                        zipCode = ZipCode(""),
                    ),
                ),

                products = listOf(),
            ),
        )

        val order = persistence.getOne(orderNo)

        requireNotNull(order)

        with(order) {
            no shouldBe OrderNo("ORDER-0001")
            orderer.id shouldBe "CUSTOMER-0001"
        }
    }
}
