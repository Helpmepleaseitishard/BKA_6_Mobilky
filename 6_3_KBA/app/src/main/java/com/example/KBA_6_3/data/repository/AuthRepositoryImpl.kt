package com.example.KBA_6_3.data.repository

import com.example.KBA_6_3.data.api.AuthApi
import com.example.KBA_6_3.data.datastore.TokenManager
import com.example.KBA_6_3.domain.entity.User
import com.example.KBA_6_3.domain.repository.AuthRepository
import kotlinx.serialization.InternalSerializationApi

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    @OptIn(InternalSerializationApi::class)
    override suspend fun login(username: String, password: String): String {
        val response = api.login(username, password)
        val token = response.accessToken
        saveToken(token)
        return token
    }

    override suspend fun getAllUsers(token: String): List<User> {
        return api.getAllUsers(token).map { dto ->
            User(
                id = dto.id,
                firstName = dto.firstName,
                lastName = dto.lastName,
                username = dto.username,
                email = dto.email,
                image = dto.image
            )
        }
    }

    override suspend fun getUserById(userId: Int, token: String): User {
        val dto = api.getUserById(userId, token)
        return User(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            username = dto.username,
            email = dto.email,
            image = dto.image
        )
    }

    override suspend fun saveToken(token: String) = tokenManager.saveToken(token)
    override suspend fun getToken(): String? = tokenManager.getToken()
    override suspend fun clearToken() = tokenManager.clearToken()
}