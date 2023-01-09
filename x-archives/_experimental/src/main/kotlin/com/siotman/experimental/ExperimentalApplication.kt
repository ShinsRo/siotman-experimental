package com.siotman.experimental

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExperimentalApplication

fun main(args: Array<String>) {
    runApplication<ExperimentalApplication>(*args)
}
