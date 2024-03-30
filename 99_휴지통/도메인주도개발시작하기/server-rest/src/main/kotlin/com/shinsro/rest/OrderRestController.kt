package com.shinsro.rest

import com.shinsro.orders.Order
import com.shinsro.orders.OrderNo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/order")
class OrderRestController(private val orderRestService: OrderRestService) {

    @GetMapping("/{orderNo}")
    fun findOrder(@PathVariable orderNo: OrderNo): Order? {
        return orderRestService.findOrder(orderNo)
    }

    @PostMapping
    fun order(@RequestBody request: OrderRequest): Order {
        return orderRestService.order(request)
    }
}