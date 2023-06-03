package com.shinsro.orders

import com.shinsro.common.StringValue
import com.shinsro.customers.Customer
import com.shinsro.products.Product

class Order(
    val no: OrderNo,

    val orderer: Customer,
    val shippingInfo: ShippingInfo,

    val products: List<Product>,
)

class OrderNo(value: String) : StringValue(value)
