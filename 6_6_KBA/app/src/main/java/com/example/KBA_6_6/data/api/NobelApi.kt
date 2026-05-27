package com.example.KBA_6_6.data.api

import com.example.KBA_6_6.data.dto.LoginRequest
import com.example.KBA_6_6.data.dto.LoginResponse
import com.example.KBA_6_6.data.dto.PrizeDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NobelApi(private val client: HttpClient) {

	companion object {
		private const val BASE_URL = "http://10.0.2.2:8080"

		fun create(): NobelApi {
			val client = HttpClient {
				install(ContentNegotiation) {
					json(Json {
						ignoreUnknownKeys = true
						isLenient = true
						encodeDefaults = false
						coerceInputValues = true
					})
				}
			}
			return NobelApi(client)
		}
	}

	var token: String? = null

	suspend fun login(username: String, password: String): Boolean {
		return try {
			val response: LoginResponse = client.post("$BASE_URL/login") {
				contentType(ContentType.Application.Json)
				setBody(LoginRequest(username, password))
			}.body()
			token = response.token
			true
		} catch (e: Exception) {
			false
		}
	}

	suspend fun getPrizes(): List<PrizeDto> {
		return client.get("$BASE_URL/prizes") {
			token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
		}.body()
	}

	suspend fun getUserFavorites(): List<PrizeDto> {
		return client.get("$BASE_URL/users/me/prizes") {
			token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
		}.body()
	}

	suspend fun addFavorite(prizeId: Int): Boolean {
		return try {
			client.post("$BASE_URL/users/me/prizes/$prizeId") {
				token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
			}
			true
		} catch (e: Exception) {
			false
		}
	}

	suspend fun removeFavorite(prizeId: Int): Boolean {
		return try {
			client.delete("$BASE_URL/users/me/prizes/$prizeId") {
				token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
			}
			true
		} catch (e: Exception) {
			false
		}
	}
}