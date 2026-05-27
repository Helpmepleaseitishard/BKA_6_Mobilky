package com.example.domain.usecase

import com.example.domain.repository.NobelPrizeRepository

class RemoveUserFavoriteUseCase(private val repository: NobelPrizeRepository) {
	suspend operator fun invoke(userId: Int, prizeId: Int): Boolean =
		repository.removeUserFavorite(userId, prizeId)
}