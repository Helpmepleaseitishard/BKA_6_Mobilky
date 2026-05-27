package com.example.KBA_6_3.domain.usecase

import com.example.KBA_6_3.domain.entity.User
import com.example.KBA_6_3.domain.repository.AuthRepository

class GetUserDetailUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userId: Int, token: String): User {
        return repository.getUserById(userId, token)
    }
}