package com.shinsro.rest

import com.shinsro.customers.Customer
import com.shinsro.orders.OrderNo

// 바디에 있는게 어색하지만 일단 테스트용으로~
data class OrderRequest(
    val no: OrderNo,
    val orderer: Customer
)
