import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import data.repository.InMemoryNobelPrizeRepository
import domain.usecase.GetLaureatesByPrizeUseCase
import domain.usecase.GetPrizeDetailUseCase
import domain.usecase.GetPrizesUseCase
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import presentation.auth.JwtConfig
import presentation.routes.authRoutes
import presentation.routes.prizeRoutes

fun main() {
	embeddedServer(Netty, port = 8080) {
		module()
	}.start(wait = true)
}

fun Application.module() {
	// Repositories and Use Cases
	val repository = InMemoryNobelPrizeRepository()
	val getPrizesUseCase = GetPrizesUseCase(repository)
	val getPrizeDetailUseCase = GetPrizeDetailUseCase(repository)
	val getLaureatesByPrizeUseCase = GetLaureatesByPrizeUseCase(repository)

	// Plugins
	install(ContentNegotiation) { json() }

	install(CallLogging) {
		level = Level.INFO
		format { call ->
			val method = call.request.httpMethod.value
			val path = call.request.path()
			val status = call.response.status()?.value ?: 0
			"$method $path - $status"
		}
	}

	install(CORS) {
		anyHost()
		allowMethod(HttpMethod.Options)
		allowMethod(HttpMethod.Get)
		allowMethod(HttpMethod.Post)
		allowMethod(HttpMethod.Put)
		allowMethod(HttpMethod.Delete)
		allowHeader(HttpHeaders.ContentType)
		allowHeader(HttpHeaders.Authorization)
		allowCredentials = true
	}

	install(StatusPages) {
		exception<Throwable> { call, cause ->
			call.respond(HttpStatusCode.InternalServerError, mapOf("error" to cause.message))
		}
	}

	install(Authentication) {
		jwt("auth-jwt") {
			verifier(
				JWT.require(Algorithm.HMAC256(JwtConfig.SECRET))
					.withIssuer(JwtConfig.ISSUER)
					.build()
			)
			validate { credential ->
				if (credential.payload.getClaim("username").asString() != null) {
					JWTPrincipal(credential.payload)
				} else null
			}
		}
	}

	routing {
		get("/") {
			call.respondText(
				text = """
                    Nobel Prize API (Clean Architecture)
                    
                    Available endpoints:
                    POST /auth/login - get JWT token
                    GET /prizes - list all prizes
                    GET /prizes/{year}/{category} - get prize details
                    GET /prizes/{year}/{category}/laureates - get laureates list
                    
                    Credentials: admin/password or user/user123
                """.trimIndent(),
				contentType = ContentType.Text.Plain
			)
		}

		get("/health") {
			call.respond(mapOf("status" to "OK"))
		}

		authRoutes()
		prizeRoutes(getPrizesUseCase, getPrizeDetailUseCase, getLaureatesByPrizeUseCase)
	}
}