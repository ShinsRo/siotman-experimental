package com.siotman.config.retrofit

import com.fasterxml.jackson.databind.ObjectMapper
import com.siotman.config.serdes.SerDes
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

abstract class AbstractRetrofitConfig(
    protected val connTimeout: Long = 5,
    protected val readTimeout: Long = 10,
    protected val logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
) {
    protected val converter: Converter.Factory
        get() = JacksonConverterFactory.create(SerDes.objectMapper)

    protected val defaultClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .connectTimeout(connTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(requestHeaderInterceptor)
            .let { interceptors.forEach(it::addInterceptor); it }
            .build()

    protected fun build(baseUrl: String, client: OkHttpClient = defaultClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converter)
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }

    protected val interceptors: Collection<Interceptor> = emptyList()

    protected val loggingInterceptor: Interceptor
        get() = HttpLoggingInterceptor().apply { this.level = logLevel }

    protected val requestHeaderInterceptor: Interceptor
        get() = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .headers(original.headers)
                .method(original.method, original.body)

            chain.proceed(builder.build())
        }
}
