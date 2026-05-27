package domain.repository

import domain.model.Laureate
import domain.model.NobelPrize

interface NobelPrizeRepository {
	fun getAllPrizes(): List<NobelPrize>
	fun getPrizeByYearAndCategory(year: String, category: String): NobelPrize?
	fun getLaureatesByPrize(year: String, category: String): List<Laureate>
}