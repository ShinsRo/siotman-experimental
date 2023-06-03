package com.shinsro.privacies

import com.shinsro.common.StringValue

class Address(
    val base: String,
    val detail: String,
    val zipCode: ZipCode,

    val nation: String? = null,
    val city: String? = null,
)

class ZipCode(value: String) : StringValue(value)
