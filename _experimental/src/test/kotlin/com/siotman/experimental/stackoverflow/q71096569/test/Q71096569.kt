package com.siotman.experimental.stackoverflow.q71096569.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean

@SpringBootTest
class Q71096569 {
    @SpyBean
    private lateinit var orderRepository: OrderRepository

    @Test
    fun test() {
        orderRepository.findAll()
    }
}