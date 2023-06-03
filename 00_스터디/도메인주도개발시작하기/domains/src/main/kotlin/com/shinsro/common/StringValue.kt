package com.shinsro.common

import java.io.Serializable

abstract class StringValue(private val value: String) : CharSequence by value, Serializable {
    override fun toString() = value
    override fun hashCode() = value.hashCode()
    override fun equals(other: Any?): Boolean {
        if (value == other) return true
        if (this === other) return true

        if (other !is StringValue) return false
        return value == other.value
    }
}
