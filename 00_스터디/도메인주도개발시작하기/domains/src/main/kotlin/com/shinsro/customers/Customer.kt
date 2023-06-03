package com.shinsro.customers

import com.shinsro.common.StringValue
import com.shinsro.privacies.PersonName

class Customer(
    val id: CustomerId,
    val customerName: CustomerName,
)

class CustomerId(value: String) : StringValue(value)
class CustomerName(value: String) : PersonName(value)
