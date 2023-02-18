package com.shinsro.encryption.core

import java.security.*
import javax.crypto.Cipher

object Rsa2048 {
    private const val ALGORITHM = "RSA"
    private const val KEY_SIZE = 2048

    val keyFactory: KeyFactory
        get() = KeyFactory.getInstance(ALGORITHM)

    fun encrypt(plain: ByteArray, pub: PublicKey): ByteArray {
        val cipher = getCipher(Cipher.ENCRYPT_MODE, pub)
        return cipher.doFinal(plain)
    }

    fun decrypt(encrypted: ByteArray, prv: PrivateKey): ByteArray {
        val cipher = getCipher(Cipher.DECRYPT_MODE, prv)
        return cipher.doFinal(encrypted)
    }

    private fun getCipher(mode: Int, key: Key): Cipher {
        return Cipher.getInstance(ALGORITHM)
            .apply { init(mode, key) }
    }

    fun genKeyPair(): KeyPair {
        return KeyPairGenerator.getInstance(ALGORITHM)
            .apply { initialize(KEY_SIZE, SecureRandom()) }
            .genKeyPair()
    }
}