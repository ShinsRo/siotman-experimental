package com.siotman.config.serdes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SerDes {
    private val localDateSer = LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE)
    private val localDateDes = LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE)
    private val localDateTimeSer = LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private val localDateTimeDes = LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    private val dateModule = SimpleModule().apply {
        addSerializer(LocalDate::class.java, localDateSer)
        addDeserializer(LocalDate::class.java, localDateDes)
        addSerializer(LocalDateTime::class.java, localDateTimeSer)
        addDeserializer(LocalDateTime::class.java, localDateTimeDes)
    }

    private val kotlinModule = KotlinModule.Builder().build()

    val objectMapper = ObjectMapper().apply {
        registerModules(
            dateModule,
            kotlinModule
        )
    }
}

