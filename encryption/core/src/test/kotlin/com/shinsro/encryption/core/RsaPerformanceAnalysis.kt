@file:Suppress("NonAsciiCharacters")

package com.shinsro.encryption.core

import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class RsaPerformanceAnalysis {

    @Test
    fun `주어진 평문에 대해 Rsa2048 암호화할 수 있다`() {
        val plain = "플레인 텍스트 - Plain Text - !@#$ - 1234"
        val keys = Rsa2048.genKeyPair()


        val encrypted: ByteArray
        val encryptionElapsed = measureTimeMillis {
            encrypted = Rsa2048.encrypt(plain.toByteArray(), keys.public)
        }

        val decrypted: ByteArray
        val decryptionElapsed = measureTimeMillis {
            decrypted = Rsa2048.decrypt(encrypted, keys.private)
        }

        println(String(decrypted))
        println(
            """
            $encryptionElapsed
            $decryptionElapsed
            
            평문 : $plain
            암호화 값 (Bytes) : $encrypted
            복호화 값 (Bytes) : $decrypted
            
            복호화 값 (String) : ${String(decrypted)}
        """.trimIndent()
        )

    }

}