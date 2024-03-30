package com.shinsro.orders

interface OrderPersistence {
    fun save(order: Order): Order
    fun findByIdOrNull(no: OrderNo): Order?
}
