package com.siotman.google

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GwGoogleApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(GwGoogleApiApplication::class.java, *args)
}