package com.siotman.spring.mapping

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class ApiVersion(val from: String, val to: String = "")
