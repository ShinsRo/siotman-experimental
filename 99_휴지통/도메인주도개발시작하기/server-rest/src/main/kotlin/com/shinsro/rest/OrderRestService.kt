package com.shinsro.rest

import com.shinsro.orders.Order
import com.shinsro.orders.OrderNo
import com.shinsro.orders.OrderPersistence
import org.springframework.stereotype.Service

@Service
class OrderRestService(private val orderPersistence: OrderPersistence) {
    fun findOrder(orderNo: OrderNo): Order? {
        return orderPersistence.findByIdOrNull(orderNo)
    }

    fun order(request: OrderRequest): Order {
        val order = Order(
            no = request.no,
            orderer = request.orderer
        )

        return orderPersistence.save(order)
    }
}