package com.shinsro.orders

interface OrderPersistence {
    fun save(order: Order): Order
    fun getOne(no: OrderNo): Order?
    fun update(order: Order): Order
}
