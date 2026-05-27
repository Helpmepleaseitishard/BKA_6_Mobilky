package domain.usecase

import domain.model.NobelPrize
import domain.repository.NobelPrizeRepository

class GetPrizesUseCase(private val repository: NobelPrizeRepository) {
	operator fun invoke(): List<NobelPrize> = repository.getAllPrizes()
}