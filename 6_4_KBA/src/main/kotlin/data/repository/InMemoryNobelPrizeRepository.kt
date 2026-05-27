package data.repository

import domain.model.Laureate
import domain.model.NobelPrize
import domain.repository.NobelPrizeRepository

class InMemoryNobelPrizeRepository : NobelPrizeRepository {

	private val prizes: List<NobelPrize> = listOf(
		NobelPrize(
			year = "2010",
			category = "physics",
			categoryFullName = "Physics",
			laureates = listOf(
				Laureate(
					id = "1",
					fullName = "Oleg Simonov",
					motivation = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter",
					country = "France"
				),
				Laureate(
					id = "2",
					fullName = "Simon Luker",
					motivation = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter",
					country = "Hungary"
				),
				Laureate(
					id = "3",
					fullName = "Anne Kowalski",
					motivation = "for experimental methods that generate attosecond pulses of light for the study of electron dynamics in matter",
					country = "France"
				)
			)
		),
		NobelPrize(
			year = "2010",
			category = "peace",
			categoryFullName = "Peace",
			laureates = emptyList()   // no laureates for peace in the provided data
		),
		NobelPrize(
			year = "2012",
			category = "literature",
			categoryFullName = "Literature",
			laureates = listOf(
				Laureate(
					id = "5",
					fullName = "Mark Twen",
					motivation = "for the courage and clinical acuity with which she uncovers the roots, estrangements and collective restraints of personal memory",
					country = "France"
				)
			)
		)
	)

	override fun getAllPrizes(): List<NobelPrize> = prizes

	override fun getPrizeByYearAndCategory(year: String, category: String): NobelPrize? =
		prizes.find { it.year == year && it.category == category.lowercase() }

	override fun getLaureatesByPrize(year: String, category: String): List<Laureate> =
		getPrizeByYearAndCategory(year, category)?.laureates ?: emptyList()
}