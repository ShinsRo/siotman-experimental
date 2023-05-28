package com.siotman.experimental.ktor.client

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.junit.jupiter.api.Test
import java.util.*

class KtorClientSetUpTests {

    @Test
    fun `Ktor Set Up - Engine Factory`() {
        val client = HttpClient()
        // 생성자에 엔진 팩토리를 명시하지 않으면, 빌드 스크립트 아티팩트를 통해 자동 결정합니다.

        client.engine should beInstanceOf(OkHttpEngine::class)
        // 필자는 OkHttp 를 가져왔습니다.
        // 따라서 OkHttpEngineFactory 가 엔진 팩토리이고, clinet.engine 은 OkHttpEngine 입니다.

        val explicitClient = HttpClient(OkHttp)
        // 여전히, 엔진 팩토리를 명시하여 정의할 수 있습니다.

        explicitClient.engine should beInstanceOf(OkHttpEngine::class)
        // 당연히, 엔진 팩토리는 OkHttpEngineFactory 이고, 엔진은 OkHttpEngine 이겠습니다.
    }

    @Test
    fun `Ktor Set Up - Engine Factories`() {
        // build.gradle.kts 에서는 아래와 같은 순서로 임포트했습니다.
        // implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        // implementation("io.ktor:ktor-client-apache:$ktorVersion")
        // implementation("io.ktor:ktor-client-cio:$ktorVersion")

        val engines = HttpClientEngineContainer::class.java
            .let { ServiceLoader.load(it, it.classLoader).toList() }
        // 사전에, OkHttp, Apache, CIO 엔진을 의존성으로 가져왔습니다.
        // 참고로, ktor 는 위와 동일한 코드로 클래스들을 로딩합니다.

        engines.count() shouldBe 3
        // 3개를 가져왔으니, 우리가 볼 수 있는 HttpClientEngineContainer 는 총 3개입니다.

        with(engines.map(HttpClientEngineContainer::toString)) {
            this[0] shouldBe "OkHttp"
            this[1] shouldBe "Apache"
            this[2] shouldBe "CIO"
        }
        // build.gradle.kts 에서 임포트한 순서와 같았습니다.

        val client = HttpClient()
        // 생성자에 엔진 팩토리를 명시하지 않고 결정하게 합니다.

        client.engine should beInstanceOf(OkHttpEngine::class)
        // build.gradle.kts 상 맨 상단에 적은 의존성을 명시해야 테스트는 통과하였습니다.
    }

    @Test
    fun `Ktor Set Up - Engine Config, OkHttp`() {
        HttpClient(OkHttp) {

            engine {
                // common 프로퍼티
                threadsCount = 8 // default : Int = 4
                pipelining = true // default : Boolean = false
                proxy = ProxyBuilder.http("") // default : ProxyConfig? = null

                // OkHttp https://ktor.io/docs/http-client-engines.html#okhttp
                // OkHttpConfig https://api.ktor.io/ktor-client/ktor-client-okhttp/io.ktor.client.engine.okhttp/-ok-http-config/index.html?_ga=2.250086022.1082379539.1673918991-752697833.1673561447&_gl=1*9p2cne*_ga*NzUyNjk3ODMzLjE2NzM1NjE0NDc.*_ga_9J976DJZ68*MTY3MzkyNjE3OC44LjEuMTY3MzkyNjc5My42MC4wLjA.
                clientCacheSize = 5 // default : Int = 10
                preconfigured // default : OkHttpClient? = null
                webSocketFactory // default : WebSocket.Factory? = null

                // 인터셉터 추가
                addInterceptor { chain ->
                    chain.proceed(chain.request())
                }

                // 네트워크 인터셉터 추가
                addNetworkInterceptor { chain ->
                    chain.proceed(chain.request())
                }

                // OkHttpClient.Builder 를 이용한 설정
                config {
                    // this: OkHttpClient.Builder
                }
            }
        }
    }
}