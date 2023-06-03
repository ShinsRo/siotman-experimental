package com.shinsro.orders.jpa

import com.shinsro.customers.Customer
import com.shinsro.customers.CustomerId
import com.shinsro.customers.CustomerName
import com.shinsro.orders.Order
import com.shinsro.orders.OrderNo
import com.shinsro.orders.ShippingInfo
import com.shinsro.privacies.Address
import com.shinsro.privacies.PersonName
import com.shinsro.privacies.ZipCode
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    val no: String,
    val ordererId: String,

    // TODO : shippingInfo, products
) {
    constructor(order: Order) : this(
        no = order.no.toString(),
        ordererId = order.orderer.id.toString(),
    )

    fun toDomain(): Order {
        return Order(
            no = OrderNo(no),

            // TODO : orderer, shippingInfo, products
            orderer = Customer(
                id = CustomerId(ordererId),
                customerName = CustomerName(""),
            ),
            shippingInfo = ShippingInfo(
                recipientName = PersonName(""),
                address = Address("", "", ZipCode("")),
            ),
            products = listOf(),
        )
    }
}
