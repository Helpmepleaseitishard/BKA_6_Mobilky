package com.example.KBA_6_3.domain.usecase

import com.example.KBA_6_3.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): String {
        return repository.login(username, password)
    }
}