package com.siotman.experimental.springhotswap

import java.time.LocalDateTime

const val staticValue = 2

object ToInfinityAndBeyond {
    private val objScopeCounter = Counter()

    fun run() {
        while (true) {
            val customer = Customer("010-1111-1111")
            val localCounter = Counter()

            println(
                """
                staticValue1 : $staticValue
                objScopeCounter : ${objScopeCounter.count()}
                methodScopeCounter : ${localCounter.count()}
                customer.phone : ${customer.phone}
                DateTimeUtils.now() : ${DateTimeUtils.now()}
            """.trimIndent()
            )

            Thread.sleep(3_000)
        }
    }
}

class Counter(private var cnt: Int = 0) {
    fun count() = --cnt
}

data class Customer(val phone: String)

object DateTimeUtils {
    fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}