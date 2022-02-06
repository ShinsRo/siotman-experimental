package com.siotman.google

import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.jvm.JvmStatic
import org.springframework.boot.SpringApplication

@SpringBootApplication
open class GwGoogleApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(GwGoogleApiApplication::class.java, *args)
}