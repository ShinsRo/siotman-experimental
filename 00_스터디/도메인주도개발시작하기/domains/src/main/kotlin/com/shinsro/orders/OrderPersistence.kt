package com.shinsro.orders

interface OrderPersistence {
    fun save(order: Order)
    fun getOne(no: OrderNo): Order?
    fun update(order: Order): Order
}
