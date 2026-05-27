package presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class PrizeResponse(
	val year: String,
	val category: String,
	val categoryFullName: String,
	val laureates: List<LaureateResponse>
)

@Serializable
data class LaureateResponse(
	val id: String,
	val fullName: String,
	val motivation: String,
	val country: String
)