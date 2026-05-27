package com.example.KBA_6_3.domain.repository

import com.example.KBA_6_3.domain.entity.User

interface AuthRepository {
    suspend fun login(username: String, password: String): String
    suspend fun getAllUsers(token: String): List<User>
    suspend fun getUserById(userId: Int, token: String): User
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}