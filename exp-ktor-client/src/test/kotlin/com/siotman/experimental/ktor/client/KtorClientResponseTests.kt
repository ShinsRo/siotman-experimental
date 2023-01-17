package com.siotman.experimental.ktor.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class KtorClientResponseTests {

    @Test
    fun `Ktor Responses - Raw Body`(): Unit = runBlocking {
        // 편의 상 서버응답을 Mocking 했습니다.
        val client = HttpClient(MockEngine { req ->
            respondOk("OK")
        })

        // Raw Body
        val response: HttpResponse = client.get("...")
        val stringBody: String = response.body()
        val byteArrayBody: ByteArray = response.body()

        // .body() 를 통한 참조는 autoCast 된 결과입니다.
        stringBody shouldBe "OK"
        byteArrayBody shouldBe "OK".toByteArray()
    }

    @Test
    fun `Ktor Responses - Json Object`(): Unit = runBlocking {
        // POJO 로 Json 바디를 읽으려면 ContentNegotiation 플러그인이 필요합니다.
        // ktor-client-content-negotiation 아티팩트에서 찾아볼 수 있습니다.
        // 위 플러그인은 Accept 와 Content-Type 헤더를 통한 요청 / 응답 사이 [역]직렬화를 지원합니다.
        data class Customer(val id: Int, val first: String, val second: String)

        val customer = Customer(2, "Shins", "Ro")
        val customerJson = ObjectMapper().writeValueAsString(customer)

        // 편의 상 서버응답을 Mocking 했습니다.
        val jacksonSupportedClient = HttpClient(MockEngine { req ->
            respond(
                content = customerJson,
                headers = headersOf("Content-Type" to listOf("application/json"))
            )
        }) {
            // 아래처럼, 플러그인을 설치하여 유틸리티를 사용할 수 있습니다.
            install(ContentNegotiation) {
                jackson()
            }
        }

        val respondCustomer: Customer = jacksonSupportedClient.get("...") {
            accept(ContentType.Application.Json)
        }.body()

        respondCustomer shouldBe customer
    }
}