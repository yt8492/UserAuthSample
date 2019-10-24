package com.yt8492.model

import io.ktor.auth.Principal

data class User(
    val username: Username,
    val password: Password
) : Principal