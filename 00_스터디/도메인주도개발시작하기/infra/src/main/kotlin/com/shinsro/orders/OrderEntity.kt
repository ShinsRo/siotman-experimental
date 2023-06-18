package com.shinsro.orders

import com.shinsro.customers.Customer
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    val no: OrderNo,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "id", column = Column(name = "orderer_id")),
        AttributeOverride(name = "customerName", column = Column(name = "orderer_name"))
    )
    val orderer: Customer
) {
    constructor(order: Order) : this(
        no = order.no,
        orderer = order.orderer,
    )

    fun toDomain() = Order(
        no = no,
        orderer = orderer
    )
}
