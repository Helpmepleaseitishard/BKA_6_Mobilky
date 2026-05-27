package com.example

import com.example.data.database.DatabaseFactory
import com.example.data.repository.PostgresNobelPrizeRepository
import com.example.domain.repository.NobelPrizeRepository
import com.example.domain.usecase.*
import com.example.presentation.auth.configureJWTAuthentication
import com.example.presentation.routes.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import io.ktor.server.auth.jwt.JWTPrincipal

fun main() {
	embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
	// Initialize database
	DatabaseFactory.init()

	val repository: NobelPrizeRepository = PostgresNobelPrizeRepository()
	val getPrizesUseCase = GetPrizesUseCase(repository)
	val getPrizeByIdUseCase = GetPrizeByIdUseCase(repository)
	val loginUseCase = LoginUseCase(repository)
	val getUserFavoritesUseCase = GetUserFavoritesUseCase(repository)
	val addUserFavoriteUseCase = AddUserFavoriteUseCase(repository)
	val removeUserFavoriteUseCase = RemoveUserFavoriteUseCase(repository)

	install(ContentNegotiation) {
		json(Json {
			prettyPrint = true
			isLenient = true
		})
	}

	install(CallLogging) {
		level = Level.INFO
		format { call ->
			"${call.request.httpMethod.value} ${call.request.uri} - ${call.response.status()}"
		}
	}

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			call.respond(
				io.ktor.http.HttpStatusCode.InternalServerError,
				mapOf("error" to cause.localizedMessage)
			)
		}
	}

	install(Authentication) {
		configureJWTAuthentication(this)
	}

	routing {
		authRoutes(loginUseCase)

		authenticate("auth-jwt") {
			get("/prizes") {
				val prizes = getPrizesUseCase()
				call.respond(prizes)
			}

			get("/prizes/{prizeId}") {
				val prizeId = call.parameters["prizeId"]?.toIntOrNull()
				if (prizeId == null) {
					call.respond(io.ktor.http.HttpStatusCode.BadRequest, mapOf("error" to "Invalid prize ID"))
					return@get
				}

				val prize = getPrizeByIdUseCase(prizeId)
				if (prize != null) {
					call.respond(prize)
				} else {
					call.respond(io.ktor.http.HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
				}
			}

			userRoutes(getUserFavoritesUseCase, addUserFavoriteUseCase, removeUserFavoriteUseCase)
		}
	}
}