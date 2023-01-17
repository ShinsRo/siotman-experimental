package com.siotman.experimental

import java.time.LocalDateTime

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        println(
            """
            멍텅구리 엔트리포인트 
            현재시간 : ${LocalDateTime.now()}
            
            이 어플리케이션은 테스트용임.
            모든 테스트는 junit 환경에서 진행.
            """.trimIndent()
        )
    }
}