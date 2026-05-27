package com.example.domain.usecase

import com.example.domain.model.NobelPrize
import com.example.domain.repository.NobelPrizeRepository

class GetPrizeByIdUseCase(private val repository: NobelPrizeRepository) {
	suspend operator fun invoke(prizeId: Int): NobelPrize? = repository.getPrizeById(prizeId)
}