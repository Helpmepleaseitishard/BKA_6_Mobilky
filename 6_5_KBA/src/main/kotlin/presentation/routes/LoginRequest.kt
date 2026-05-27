package com.example.presentation.routes

import com.example.domain.usecase.*
import com.example.presentation.auth.generateToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

fun Route.authRoutes(loginUseCase: LoginUseCase) {
	post("/login") {
		val login = call.receive<LoginRequest>()
		val user = loginUseCase(login.username, login.password)

		if (user != null) {
			val token = generateToken(user.id, user.username, user.role)
			call.respond(HttpStatusCode.OK, mapOf("token" to token))
		} else {
			call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
		}
	}
}