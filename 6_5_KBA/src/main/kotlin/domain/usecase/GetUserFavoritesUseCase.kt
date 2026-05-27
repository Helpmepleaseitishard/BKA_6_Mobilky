package com.example.domain.usecase

import com.example.domain.model.NobelPrize
import com.example.domain.repository.NobelPrizeRepository

class GetUserFavoritesUseCase(private val repository: NobelPrizeRepository) {
	suspend operator fun invoke(userId: Int): List<NobelPrize> = repository.getUserFavorites(userId)
}