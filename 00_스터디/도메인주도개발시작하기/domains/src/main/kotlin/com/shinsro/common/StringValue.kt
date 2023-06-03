package com.shinsro.common

import java.io.Serializable

abstract class StringValue(private val value: String) : CharSequence by value, Serializable {
    override fun toString() = value
}
