package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Laureate(
	val id: Int = 0,
	val prizeId: Int = 0,
	val fullName: String,
	val portion: String,
	val motivation: String,
	val portraitUrl: String = ""
)