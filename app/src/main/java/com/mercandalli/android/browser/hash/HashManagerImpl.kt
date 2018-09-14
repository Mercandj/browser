package com.mercandalli.android.browser.hash

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

class HashManagerImpl : HashManager {

    private val charsetIso88591 by lazy { charset("iso-8859-1") }

    override fun sha256(
            text: String?,
            time: Int
    ): String? {
        if (text == null) {
            return null
        }
        var result = text
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            return null
        }

        for (i in 0 until time) {
            result = hash(messageDigest, result)
        }
        return result
    }

    private fun hash(
            messageDigest: MessageDigest,
            text: String?
    ): String? {
        if (text == null) {
            return null
        }
        try {
            messageDigest.update(text.toByteArray(charsetIso88591), 0, text.length)
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val digest = messageDigest.digest()
        return convertToHex(digest)
    }

    private fun convertToHex(data: ByteArray): String {
        val buf = StringBuilder()
        for (e in data.indices) {
            val b = (data[e] and 0xFF.toByte()).toInt()
            var halfByte = (b ushr 4) and 0x0F
            var twoHalf = 0
            do {
                val char = if (halfByte in 0..9) '0'.toInt() + halfByte
                else 'a'.toInt() + (halfByte - 10)
                buf.append(char.toChar())
                halfByte = b and 0x0F
            } while (twoHalf++ < 1)
        }
        return buf.toString()
    }
}