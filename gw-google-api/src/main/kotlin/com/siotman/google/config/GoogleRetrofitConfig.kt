package com.siotman.google.config

import com.siotman.common.retrofit.AbstractRetrofitConfig
import com.siotman.google.service.calendar.CalendarApi
import com.siotman.google.service.oauth.OauthApi
import com.siotman.google.service.tasks.TasksApi
import okhttp3.Interceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleRetrofitConfig : AbstractRetrofitConfig() {

    val oauthApi: OauthApi
        @Bean get() = build("https://www.googleapis.com/").create(OauthApi::class.java)

    val tasksApi: TasksApi
        @Bean get() = build("https://tasks.googleapis.com/").create(TasksApi::class.java)

    val calendarApi: CalendarApi
        @Bean get() = build("https://www.googleapis.com/").create(CalendarApi::class.java)


    override val requestHeaderInterceptor: Interceptor
        get() = Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().build())
        }
}
