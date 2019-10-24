package com.yt8492

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.yt8492.model.LoginRequest
import com.yt8492.model.User
import com.yt8492.model.Username
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import org.slf4j.event.Level
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val ApplicationCall.user
    get() = authentication.principal<User>() ?: throw IllegalStateException("unauthorized")

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        serialization()
    }
    install(CallLogging) {
        level = Level.DEBUG
    }
    install(Authentication) {
        jwt {
            verifier(JWT.require(Algorithm.HMAC256("ktor")).withIssuer("ktor").build())
            realm = "ktor"
            validate { credential ->
                UserRepository.findByUsername(credential.payload.getClaim("username").asString().let { Username(it) })
            }
        }
    }
    routing {
        authenticate {
            get("/user") {
                call.respond(HttpStatusCode.OK, call.user.toString())
            }
        }
        post("/login") {
            val principal = call.receive<LoginRequest>()
            val token = JWT.create()
                .withSubject("id")
                .withClaim("username", principal.username)
                .withIssuer("ktor")
                .withHeader(mapOf(
                    "typ" to "JWT"
                ))
                .withExpiresAt(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant().let(Date::from))
                .sign(Algorithm.HMAC256("ktor"))
            call.respond(HttpStatusCode.OK, token)
        }
    }
}
