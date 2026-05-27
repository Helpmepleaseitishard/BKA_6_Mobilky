// PrizeDto.kt
package com.example.KBA_6_6.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PrizeDto(
	val id: Int,
	val awardYear: Int,
	val category: String,
	val fullName: String = "",
	val motivation: String = "",
	val detailLink: String = "",
	val laureates: List<LaureateDto> = emptyList()
)

@Serializable
data class LaureateDto(
	val id: Int = 0,
	val prizeId: Int = 0,
	val fullName: String,
	val portion: String,
	val motivation: String,
	val portraitUrl: String = ""
)