package com.example.KBA_6_3.data.api

import com.example.KBA_6_3.data.dto.LoginRequestDto
import com.example.KBA_6_3.data.dto.LoginResponseDto
import com.example.KBA_6_3.data.dto.UserDto
import com.example.KBA_6_3.data.dto.UsersResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.InternalSerializationApi

class AuthApi(private val client: HttpClient) {

    @OptIn(InternalSerializationApi::class)
    suspend fun login(username: String, password: String): LoginResponseDto {
        return client.post("https://dummyjson.com/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(username, password))
        }.body()
    }

    suspend fun getAllUsers(token: String): List<UserDto> {
        return client.get("https://dummyjson.com/users") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body<UsersResponseDto>().users
    }

    suspend fun getUserById(userId: Int, token: String): UserDto {
        return client.get("https://dummyjson.com/users/$userId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body()
    }
}