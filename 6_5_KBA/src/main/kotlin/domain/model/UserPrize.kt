package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPrize(
	val userId: Int,
	val prizeId: Int,
	val addedAt: String = ""
)