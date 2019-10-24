package com.yt8492.model

import java.security.MessageDigest

data class Password(val hashedValue: String) {
    companion object {
        fun toHash(value: String): String {
            val salt = "ktor"
            return MessageDigest.getInstance("SHA-256")
                .digest((value + salt).toByteArray())
                .joinToString("") {
                    "%02x".format(it)
                }
        }

        fun isSame(hashedValue: String, rowValue: String): Boolean =
            hashedValue == toHash(rowValue)
    }
}