package com.example.KBA_6_3.data.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class LoginResponseDto(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val image: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)