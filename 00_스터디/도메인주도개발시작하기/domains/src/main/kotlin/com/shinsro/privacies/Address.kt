package com.shinsro.privacies

class Address(
    val base: String,
    val detail: String,
    val zipCode: ZipCode,

    val nation: String? = null,
    val city: String? = null
)

class ZipCode(private val value: String): CharSequence by value