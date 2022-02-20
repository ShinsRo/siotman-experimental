package com.siotman.config.retrofit

import retrofit2.Call
import retrofit2.Response

class RetrofitUtils

fun <T> Call<T>.response(
    onSuccess: (Response<T>) -> T = ::defaultCallSuccessHandler,
    onFailure: (Response<T>) -> T? = ::defaultCallFailureHandler
): T? {
    val response = execute()

    return when {
        response.isSuccessful -> onSuccess(response)
        else -> onFailure(response)
    }
}

private fun <T> defaultCallSuccessHandler(res: Response<T>) = res.body()!!

private fun <T> defaultCallFailureHandler(res: Response<T>): T? {
    throw RuntimeException(
        """
        API 통신 중 알 수 없는 오류가 발생했습니다.
        
        statusCode : ${res.code()}
        body : ${res.body() ?: res.errorBody()}
        """.trimIndent()
    )
}