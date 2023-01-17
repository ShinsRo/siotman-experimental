package com.siotman.experimental.ktor.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class KtorClientTests {
//
//    private val client = HttpClient(OkHttp) {
//        install(Logging) {
//            logger = Logger.SIMPLE
//            level = LogLevel.HEADERS
//        }
//    }
//
//    @Test
//    fun test() = runBlocking {
//        val response = client.get {
//            url {
//                protocol = URLProtocol.HTTPS
//                host = "www.naver.com"
//                path("/")
//            }
//        }
//
//        println(response.body() as String)
//    }
//
//    @Test
//    fun ``() = runBlocking {
//
//    }
//
}