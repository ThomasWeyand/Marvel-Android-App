package br.com.thomas.weyandmarvel.data.repository.keyAuthentication

import timber.log.Timber
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

object HashHelper {

    fun generateHash(value: String): String {

        return try {
            md5(value).toHex()
        } catch (ex: Exception) {
            Timber.e(ex, "HashHelper")
            ""
        }
    }

    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

    private fun md5(str: String): ByteArray =
        MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
}