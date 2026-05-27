package com.example.presentation.routes

import com.example.domain.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable

@Serializable
data class AddFavoriteRequest(val prizeId: Int)

fun Route.userRoutes(
	getUserFavoritesUseCase: GetUserFavoritesUseCase,
	addUserFavoriteUseCase: AddUserFavoriteUseCase,
	removeUserFavoriteUseCase: RemoveUserFavoriteUseCase
) {
	authenticate("auth-jwt") {
		get("/users/me") {
			val principal = call.principal<JWTPrincipal>()
			val userId = principal?.payload?.getClaim("userId")?.asInt()
			val username = principal?.payload?.getClaim("username")?.asString()
			val role = principal?.payload?.getClaim("role")?.asString()

			call.respond(mapOf(
				"id" to userId,
				"username" to username,
				"role" to role
			))
		}

		get("/users/me/prizes") {
			val principal = call.principal<JWTPrincipal>()
			val userId = principal?.payload?.getClaim("userId")?.asInt() ?: return@get

			val favorites = getUserFavoritesUseCase(userId)
			call.respond(favorites)
		}

		post("/users/me/prizes/{prizeId}") {
			val principal = call.principal<JWTPrincipal>()
			val userId = principal?.payload?.getClaim("userId")?.asInt() ?: return@post

			val prizeId = call.parameters["prizeId"]?.toIntOrNull()
			if (prizeId == null) {
				call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
				return@post
			}

			val added = addUserFavoriteUseCase(userId, prizeId)
			if (added) {
				call.respond(HttpStatusCode.Created, mapOf("message" to "Prize added to favorites"))
			} else {
				call.respond(HttpStatusCode.Conflict, mapOf("error" to "Prize already in favorites"))
			}
		}

		delete("/users/me/prizes/{prizeId}") {
			val principal = call.principal<JWTPrincipal>()
			val userId = principal?.payload?.getClaim("userId")?.asInt() ?: return@delete

			val prizeId = call.parameters["prizeId"]?.toIntOrNull()
			if (prizeId == null) {
				call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
				return@delete
			}

			val removed = removeUserFavoriteUseCase(userId, prizeId)
			if (removed) {
				call.respond(HttpStatusCode.OK, mapOf("message" to "Prize removed from favorites"))
			} else {
				call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found in favorites"))
			}
		}
	}
}