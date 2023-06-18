package com.shinsro.rest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RestSpringBootApplication

fun main(args: Array<String>) {
    SpringApplication.run(RestSpringBootApplication::class.java, *args)
}