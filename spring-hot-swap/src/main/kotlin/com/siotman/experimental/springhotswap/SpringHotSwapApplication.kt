package com.siotman.experimental.springhotswap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringHotSwapApplication

fun main(args: Array<String>) {
    runApplication<SpringHotSwapApplication>(*args)
    ToInfinityAndBeyond.run()
}
