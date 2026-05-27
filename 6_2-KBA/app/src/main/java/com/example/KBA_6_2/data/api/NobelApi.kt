package com.example.KBA_6_2.data.api

import com.example.KBA_6_2.data.dto.NobelResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NobelApi(private val client: HttpClient) {

    companion object {
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

    suspend fun getNobelPrizes(
        limit: Int = 100,
        offset: Int = 0,
        year: String? = null,
        category: String? = null
    ): NobelResponseDto {
        return client.get("https://api.nobelprize.org/2.1/nobelPrizes") {
            parameter("limit", limit)
            parameter("offset", offset)
            year?.let { parameter("nobelPrizeYear", it) }
            category?.let { parameter("nobelPrizeCategory", it) }
        }.body()
    }
}