package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NobelPrize(
	val id: Int = 0,
	val awardYear: Int,
	val category: String,
	val fullName: String = "",
	val motivation: String = "",
	val detailLink: String = "",
	val laureates: List<Laureate> = emptyList()
)