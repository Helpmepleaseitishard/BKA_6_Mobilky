package presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import presentation.auth.JwtConfig
import presentation.dto.LoginRequest
import presentation.dto.LoginResponse

// In‑memory "database" of valid credentials
private val validCredentials = mapOf("admin" to "password", "user" to "user123")

fun Route.authRoutes() {
	post("/auth/login") {
		val request = call.receive<LoginRequest>()
		if (validCredentials[request.username] == request.password) {
			val token = JwtConfig.generateToken(request.username)
			call.respond(LoginResponse(token))
		} else {
			call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
		}
	}
}