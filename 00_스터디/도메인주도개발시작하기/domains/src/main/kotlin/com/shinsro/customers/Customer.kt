package com.shinsro.customers

import com.shinsro.privacies.PersonName

class Customer(
    val id: CustomerId,
    val customerName: CustomerName,
)

class CustomerId(private val value: String) : CharSequence by value
class CustomerName(private val value: String) : PersonName(value)
