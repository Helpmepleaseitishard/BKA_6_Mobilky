package com.example.KBA_6_3.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponseDto(
    val users: List<UserDto>
)