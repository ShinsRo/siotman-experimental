@file:Suppress("SpellCheckingInspection", "NonAsciiCharacters")

package com.siotman.experimental.ktor.client

import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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
                encodedParameters.append("param2", "값") // ...&param2=%EA%B0%92

                // Fragment 정의
                fragment = "fragment1" // #fragment1
                encodedFragment = "프래그먼트2" // #프래그먼트2
            }
        }

        response.bodyAsText() shouldBe "https://example.host.com/path1/path2/path3/%ED%8C%A8%EC%8A%A43/패스4?param1=value&param2=값"
    }
}