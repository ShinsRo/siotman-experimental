package com.shinsro.orders

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class OrderPersistenceImpl(private val jpaRepository: OrderJpaRepository) : OrderPersistence {
    override fun save(order: Order): Order {
        return jpaRepository.save(OrderEntity(order)).toDomain()
    }

    override fun findByIdOrNull(no: OrderNo): Order? {
        return jpaRepository.findByIdOrNull(no)?.toDomain()
    }
}
