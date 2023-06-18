package com.shinsro.common

import java.io.Serializable

// 이런식은 확실히 아닌듯. Converting 들에 있어 문제가 있음.
// value class ( kt 1.5 이전은 inline class ) 를 다음턴에 해봐야겠음
@NoArgConstructor
open class StringValue(private val value: String) : CharSequence by value, Serializable {
    override fun toString() = value
    override fun hashCode() = value.hashCode()
    override fun equals(other: Any?): Boolean {
        if (value == other) return true
        if (this === other) return true

        if (other !is StringValue) return false
        return value == other.value
    }
}
