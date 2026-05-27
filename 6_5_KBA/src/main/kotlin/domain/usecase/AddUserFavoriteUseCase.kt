package com.example.domain.usecase

import com.example.domain.repository.NobelPrizeRepository

class AddUserFavoriteUseCase(private val repository: NobelPrizeRepository) {
	suspend operator fun invoke(userId: Int, prizeId: Int): Boolean =
		repository.addUserFavorite(userId, prizeId)
}