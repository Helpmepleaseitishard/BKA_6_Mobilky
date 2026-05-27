package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.NobelPrizeRepository

class LoginUseCase(private val repository: NobelPrizeRepository) {
	suspend operator fun invoke(username: String, password: String): User? {
		return repository.verifyUserCredentials(username, password)
	}
}