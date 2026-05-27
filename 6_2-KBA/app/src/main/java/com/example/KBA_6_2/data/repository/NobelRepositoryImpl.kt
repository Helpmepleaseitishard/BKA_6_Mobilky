package com.example.KBA_6_2.data.repository

import com.example.KBA_6_2.data.api.NobelApi
import com.example.KBA_6_2.domain.entity.Laureate
import com.example.KBA_6_2.domain.repository.NobelRepository

class NobelRepositoryImpl(
    private val api: NobelApi
) : NobelRepository {

    override suspend fun getLaureates(
        year: String?,
        category: String?
    ): List<Laureate> {
        val response = api.getNobelPrizes(limit = 100, offset = 0, year = year, category = category)

        return response.nobelPrizes.flatMap { prize ->
            prize.laureates?.map { laureate ->
                Laureate(
                    id = laureate.id,
                    year = prize.awardYear,
                    category = prize.category.en,
                    categoryFullName = prize.categoryFullName?.en ?: prize.category.en,
                    fullName = laureate.fullName?.en ?: "Unknown",
                    motivation = laureate.motivation?.en?.take(200) ?: "",
                    fullMotivation = laureate.motivation?.en ?: "",
                    country = laureate.birth?.country?.en
                        ?: laureate.birth?.place?.country?.en
                        ?: "Unknown",
                    portraitUrl = laureate.portraitUrl
                )
            } ?: emptyList()
        }
    }
}