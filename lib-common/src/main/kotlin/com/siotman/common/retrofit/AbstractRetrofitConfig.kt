package com.siotman.common.retrofit

import com.siotman.common.serdes.SerDes
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

abstract class AbstractRetrofitConfig(
    protected val connTimeout: Long = 5,
    protected val readTimeout: Long = 10
) {
    open protected val converter: Converter.Factory
        get() = JacksonConverterFactory.create(SerDes.objectMapper)

    open protected val defaultClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .connectTimeout(connTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .addInterceptor(requestHeaderInterceptor)
            .let { interceptors.forEach(it::addInterceptor); it }
            .build()

    open protected fun build(baseUrl: String, client: OkHttpClient = defaultClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converter)
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }

    open protected val interceptors: Collection<Interceptor> = emptyList()

    open protected val requestHeaderInterceptor: Interceptor
        get() = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()

            chain.proceed(builder.build())
        }
}
