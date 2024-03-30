package com.shinsro.orders

import com.shinsro.common.StringValue
import com.shinsro.customers.Customer

class Order(
    val no: OrderNo,
    val orderer: Customer
)

class OrderNo(value: String) : StringValue(value)
