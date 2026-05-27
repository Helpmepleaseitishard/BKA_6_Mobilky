package com.example.KBA_6_6.data.repository

import com.example.KBA_6_6.data.api.NobelApi
import com.example.KBA_6_6.domain.entity.Laureate
import com.example.KBA_6_6.domain.repository.NobelRepository

class NobelRepositoryImpl(
	private val api: NobelApi
) : NobelRepository {

	override suspend fun getLaureates(
		year: String?,
		category: String?
	): List<Laureate> {
		val prizes = api.getPrizes()
		return prizes.flatMap { prize ->
			val yearOk = year == null || prize.awardYear.toString() == year
			val categoryOk = category == null || prize.category == category
			if (yearOk && categoryOk) {
				prize.laureates.map { laureateDto ->
					Laureate(
						id = laureateDto.id.toString(),
						year = prize.awardYear.toString(),
						category = prize.category,
						categoryFullName = prize.category,
						fullName = laureateDto.fullName,
						motivation = laureateDto.motivation.take(200),
						fullMotivation = laureateDto.motivation,
						country = "",
						portraitUrl = laureateDto.portraitUrl.ifEmpty { null }
					)
				}
			} else emptyList()
		}
	}
}