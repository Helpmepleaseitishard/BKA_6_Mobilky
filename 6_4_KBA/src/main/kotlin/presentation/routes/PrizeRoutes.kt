package presentation.routes

import domain.usecase.GetLaureatesByPrizeUseCase
import domain.usecase.GetPrizeDetailUseCase
import domain.usecase.GetPrizesUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import presentation.dto.LaureateResponse
import presentation.dto.PrizeResponse

fun Route.prizeRoutes(
	getPrizesUseCase: GetPrizesUseCase,
	getPrizeDetailUseCase: GetPrizeDetailUseCase,
	getLaureatesByPrizeUseCase: GetLaureatesByPrizeUseCase
) {
	authenticate("auth-jwt") {
		get("/prizes") {
			val prizes = getPrizesUseCase()
			val response = prizes.map { prize ->
				PrizeResponse(
					year = prize.year,
					category = prize.category,
					categoryFullName = prize.categoryFullName,
					laureates = prize.laureates.map { laureate ->
						LaureateResponse(
							id = laureate.id,
							fullName = laureate.fullName,
							motivation = laureate.motivation,
							country = laureate.country
						)
					}
				)
			}
			call.respond(response)
		}

		get("/prizes/{year}/{category}") {
			val year = call.parameters["year"] ?: return@get
			val category = call.parameters["category"] ?: return@get
			val prize = getPrizeDetailUseCase(year, category)

			if (prize != null) {
				call.respond(
					PrizeResponse(
						year = prize.year,
						category = prize.category,
						categoryFullName = prize.categoryFullName,
						laureates = prize.laureates.map { laureate ->
							LaureateResponse(
								id = laureate.id,
								fullName = laureate.fullName,
								motivation = laureate.motivation,
								country = laureate.country
							)
						}
					)
				)
			} else {
				call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
			}
		}

		get("/prizes/{year}/{category}/laureates") {
			val year = call.parameters["year"] ?: return@get
			val category = call.parameters["category"] ?: return@get
			val laureates = getLaureatesByPrizeUseCase(year, category)

			if (laureates.isNotEmpty()) {
				val response = laureates.map { laureate ->
					LaureateResponse(
						id = laureate.id,
						fullName = laureate.fullName,
						motivation = laureate.motivation,
						country = laureate.country
					)
				}
				call.respond(response)
			} else {
				call.respond(HttpStatusCode.NotFound, mapOf("error" to "Prize not found"))
			}
		}
	}
}