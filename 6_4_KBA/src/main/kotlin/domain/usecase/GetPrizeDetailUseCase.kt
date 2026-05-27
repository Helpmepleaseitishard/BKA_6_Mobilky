package domain.usecase

import domain.model.NobelPrize
import domain.repository.NobelPrizeRepository

class GetPrizeDetailUseCase(private val repository: NobelPrizeRepository) {
	operator fun invoke(year: String, category: String): NobelPrize? =
		repository.getPrizeByYearAndCategory(year, category)
}