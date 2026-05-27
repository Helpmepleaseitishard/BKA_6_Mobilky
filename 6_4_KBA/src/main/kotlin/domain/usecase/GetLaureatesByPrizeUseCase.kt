package domain.usecase

import domain.model.Laureate
import domain.repository.NobelPrizeRepository

class GetLaureatesByPrizeUseCase(private val repository: NobelPrizeRepository) {
	operator fun invoke(year: String, category: String): List<Laureate> =
		repository.getLaureatesByPrize(year, category)
}