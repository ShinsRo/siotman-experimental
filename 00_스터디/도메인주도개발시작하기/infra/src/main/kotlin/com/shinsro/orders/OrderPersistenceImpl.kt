package com.shinsro.orders

import com.shinsro.orders.jpa.OrderEntity
import com.shinsro.orders.jpa.OrderJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderPersistenceImpl(private val jpaRepository: OrderJpaRepository) : OrderPersistence {
    override fun save(order: Order): Order {
        return jpaRepository.save(OrderEntity(order)).toDomain()
    }

    override fun getOne(no: OrderNo): Order? {
        return jpaRepository.findByIdOrNull(no.toString())?.toDomain()
    }

    override fun update(order: Order): Order {
        return jpaRepository.save(OrderEntity(order)).toDomain()
    }
}
