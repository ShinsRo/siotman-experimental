package com.siotman.experimental.stackoverflow.q71096569.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean

@SpringBootTest
class Q71096569 {
    @SpyBean
    private lateinit var orderRepository: OrderRepository

    @SpyBean
    private lateinit var orderItemRepository: OrderItemRepository

    @Test
    fun test() {
        val order = Order().apply { this.orderName = "ORDER01" }

        val order2 = Order().apply { this.orderName = "ORDER02" }
        saveOrder(order2)

        val orderItems = listOf(
            OrderItem().apply { this.order = order; this.quantity = 1 },
            OrderItem().apply { this.order = null; this.quantity = 2 },
            OrderItem().apply { this.order = order2; this.quantity = 3 }
        )

        order.apply { this.orderItems = orderItems }

        val savedOrder = saveOrder(order)
        findOrder(savedOrder)
    }

    fun findOrder(savedOrder: Order) {
        println(savedOrder)

        val foundOrder = orderRepository.findById(savedOrder.id)
        println(foundOrder.get())

        val foundOrderItem = orderItemRepository.findAll()
        println(foundOrderItem.map { it.quantity })
        println(foundOrderItem.map { it.order?.orderName })

        println("END")
    }

    fun saveOrder(order: Order): Order {
        println("SERVICE saving order $order")
        return orderRepository.save(order)
    }
}