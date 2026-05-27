package com.example.domain.repository

import com.example.domain.model.*

interface NobelPrizeRepository {
	suspend fun getAllPrizes(): List<NobelPrize>
	suspend fun getPrizeById(prizeId: Int): NobelPrize?
	suspend fun getUserByUsername(username: String): User?
	suspend fun verifyUserCredentials(username: String, password: String): User?
	suspend fun getUserFavorites(userId: Int): List<NobelPrize>
	suspend fun addUserFavorite(userId: Int, prizeId: Int): Boolean
	suspend fun removeUserFavorite(userId: Int, prizeId: Int): Boolean
	suspend fun savePrize(prize: NobelPrize): Int
}