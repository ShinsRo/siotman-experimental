package com.siotman.experimental.exp_avro.registry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.schema.registry.EnableSchemaRegistryServer

@SpringBootApplication
@EnableSchemaRegistryServer
class StorySchemaRegistryServerApplication

fun main(args: Array<String>) {
    runApplication<StorySchemaRegistryServerApplication>(*args)
}