@file:Suppress("SpellCheckingInspection", "NonAsciiCharacters")

package com.siotman.experimental.ktor.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class KtorClientRequestTests {

    @Test
    fun `Request 생성하기 - HTTP 메소드, URL 명시`() = runBlocking {
        val engine = MockEngine { respondOk(content = "멍텅구리 Response") }
        val client = HttpClient(engine)

        val response = client.get("www.domain.com")

        println(response.body() as String)
    }

}