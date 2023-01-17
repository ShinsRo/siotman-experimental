@file:Suppress("SpellCheckingInspection", "NonAsciiCharacters")

package com.siotman.experimental.ktor.client

import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class KtorClientRequestTests {

    // 빌더 익스텐션들
    // client.request
    //
    // client.get
    // client.post
    // ... .options, .delete, .patch, .put ...
    //
    // [HEAD]
    // client.prepare...
    //
    // [POST]
    // client.submitForm...

    @Test
    fun `Ktor Requests - Http 메소드의 명시`(): Unit = runBlocking {

        // 편의 상 요청의 HttpMethod 를 String 으로 반환하는 서버응답을 Mocking 했습니다.
        val client = HttpClient(MockEngine { req ->
            respondOk(req.method.value)
        })

        // HttpClient.request
        client.request {
            method = HttpMethod.Get
        }.body() as String shouldBe "GET"

        // HttpCleint.get [.post | .option | .head | .delete ... ]
        client.get("...").body() as String shouldBe "GET"
        client.post("...").body() as String shouldBe "POST"
        client.delete("...").bodyAsText() shouldBe "DELETE"
        // 이미 구현된 extension 을 사용하는 것이 좀 더 읽기 쉬워보입니다.

    }

    @Test
    fun `Ktor Requests - Url 의 명시`(): Unit = runBlocking {

        // 편의 상 요청의 uri 를 String 으로 반환하는 서버응답을 Mocking 했습니다.
        val client = HttpClient(MockEngine { req ->
            respondOk("${req.url.protocol.name}://${req.url.host}${req.url.fullPath}")
        })

        val response = client.request {
            url { // this: URLBuilder
                protocol = URLProtocol.HTTPS
                host = "example.host.com"

                // path 정의
                // URLBuilder.path
                path("/path1/path2") // .../path1/path2

                appendPathSegments("path3", "패스3") // .../path3/%ED%8C%A8%EC%8A%A43
                appendEncodedPathSegments("패스4") // .../패스4
                // vararg 로 path 를 나누어 전달할 수 있습니다.
                // appendPathSegments 는 기본적으로 Url 인코딩합니다.
                // appendEncodedPathSegments 를 사용하여 이미 인코딩된 ( 인코딩하지 않을 ) path 를 명시할 수 있습니다.

                // Query String 정의
                // URLBuilder.parameters
                parameters.append("param1", "value") // ...?param1=value
                encodedParameters.append("param2", "값") // ...&param2=값

                // Fragment 정의
                fragment = "fragment1" // #fragment1
                encodedFragment = "프래그먼트2" // #프래그먼트2
            }
        }

        response.bodyAsText() shouldBe "https://example.host.com/path1/path2/path3/%ED%8C%A8%EC%8A%A43/패스4?param1=value&param2=값"
    }

    @Test
    fun `Ktor Requests - Headers 의 명시`(): Unit = runBlocking {

        // 편의 상 요청의 헤더를 출력하는 응답을 목킹합니다.
        val client = HttpClient(MockEngine { req ->
            respondOk(req.headers.entries().joinToString("\n"))
        })

        client.get("test.host") {
            header("Single-Header", "값") // single header

            headers {
                append(HttpHeaders.Accept, "text/plain")
                append(HttpHeaders.Authorization, "auth")
                append(HttpHeaders.UserAgent, "ktor client")
                append(HttpHeaders.AcceptCharset, "EUC-KR") // Default 값 : [UTF-8]
            } // 여러 헤더 값 추가

            // 여러 extension 메소드들도 존재합니다.
            accept(ContentType.Text.Html)
            userAgent("ktor client 2")
            cookie("email", "ShinsRo@email.com", expires = GMTDate())

        }.body() as String shouldBe """
            Single-Header=[값]
            Accept=[text/plain, text/html]
            Authorization=[auth]
            User-Agent=[ktor client 2]
            Accept-Charset=[EUC-KR]
            Cookie=[email=ShinsRo%40email.com]
        """.trimIndent().trim()
    }

    @Test
    fun `Ktor Requests - Body 의 명시`(): Unit = runBlocking {

        // 편의 상 요청의 바디를 출력하는 응답을 목킹합니다.
        val client = HttpClient(MockEngine { req ->
            respondOk(req.body.toByteReadPacket().readText())
        })

        // Plain Text
        client.post("...") {
            setBody("컨텐츠")
        }.bodyAsText() shouldBe "컨텐츠"

        // Objects - ContentNegotiation 플러그인이 필요합니다.
        // ktor-client-content-negotiation 아티팩트에서 찾아볼 수 있습니다.
        // 위 플러그인은 Accept 와 Content-Type 헤더를 통한 요청 / 응답 사이 [역]직렬화를 지원합니다.
        val jacksonSupportedClient = HttpClient(MockEngine { req ->
            respondOk(req.body.toByteReadPacket().readText())
        }) {
            // 아래처럼, 플러그인을 설치하여 유틸리티를 사용할 수 있습니다.
            install(ContentNegotiation) {
                jackson()
            }
        }

        data class Customer(val id: Int, val first: String, val second: String)

        jacksonSupportedClient.post("...") {
            contentType(ContentType.Application.Json)
            setBody(Customer(3, "Shins", "Ro"))
        }.bodyAsText() shouldBe """{"id":3,"first":"Shins","second":"Ro"}"""

        // Form
        client.submitForm(
            url = "...",
            formParameters = Parameters.build {
                append("username", "Shins")
                append("password", "snihS")
            },
            encodeInQuery = false // default : false
        ).bodyAsText() shouldBe "username=Shins&password=snihS"

    }

}