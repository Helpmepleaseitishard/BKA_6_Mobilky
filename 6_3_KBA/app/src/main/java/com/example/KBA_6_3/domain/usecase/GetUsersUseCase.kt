package com.example.KBA_6_3.domain.usecase

import com.example.KBA_6_3.domain.entity.User
import com.example.KBA_6_3.domain.repository.AuthRepository

class GetUsersUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String): List<User> {
        return repository.getAllUsers(token)
    }
}