package com.yt8492.model

import io.ktor.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
) : Principal