package com.yt8492

import com.yt8492.model.Password
import com.yt8492.model.User
import com.yt8492.model.Username

object UserRepository {

    private val users = mutableListOf(
        User(Username("id"), Password(Password.toHash("password"))),
        User(Username("yt8492"), Password(Password.toHash("password")))
    )

    fun findByUsername(username: Username): User? = users.find {
        it.username == username
    }
}