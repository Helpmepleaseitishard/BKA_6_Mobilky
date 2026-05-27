package com.example.presentation.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.Date

const val JWT_SECRET = "your-secret-key-at-least-32-characters-long!"
const val JWT_ISSUER = "nobel-prize-api"
const val JWT_AUDIENCE = "nobel-prize-client"
const val JWT_REALM = "Nobel Prize API"
const val JWT_VALIDITY_MS = 30 * 60 * 1000L

fun configureJWTAuthentication(config: AuthenticationConfig) {
	config.jwt("auth-jwt") {
		realm = JWT_REALM
		verifier(
			JWT
				.require(Algorithm.HMAC256(JWT_SECRET))
				.withIssuer(JWT_ISSUER)
				.withAudience(JWT_AUDIENCE)
				.build()
		)
		validate { credential ->
			if (credential.payload.getClaim("userId").asInt() != null) {
				JWTPrincipal(credential.payload)
			} else null
		}
	}
}

fun generateToken(userId: Int, username: String, role: String): String {
	return JWT.create()
		.withIssuer(JWT_ISSUER)
		.withAudience(JWT_AUDIENCE)
		.withClaim("userId", userId)
		.withClaim("username", username)
		.withClaim("role", role)
		.withExpiresAt(Date(System.currentTimeMillis() + JWT_VALIDITY_MS))
		.sign(Algorithm.HMAC256(JWT_SECRET))
}