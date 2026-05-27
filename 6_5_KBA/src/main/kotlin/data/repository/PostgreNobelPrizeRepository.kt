package com.example.data.repository

import com.example.data.database.*
import com.example.domain.model.*
import com.example.domain.repository.NobelPrizeRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class PostgresNobelPrizeRepository : NobelPrizeRepository {

	override suspend fun getAllPrizes(): List<NobelPrize> = transaction {
		Prizes.selectAll().map { row ->
			val prizeId = row[Prizes.id]
			val laureates = Laureates.select { Laureates.prizeId eq prizeId }.map { laureateRow ->
				Laureate(
					id = laureateRow[Laureates.id],
					prizeId = prizeId,
					fullName = laureateRow[Laureates.fullName],
					portion = laureateRow[Laureates.portion],
					motivation = laureateRow[Laureates.motivation],
					portraitUrl = laureateRow[Laureates.portraitUrl] ?: ""
				)
			}

			NobelPrize(
				id = prizeId,
				awardYear = row[Prizes.awardYear],
				category = row[Prizes.category],
				fullName = row[Prizes.fullName] ?: "",
				motivation = row[Prizes.motivation] ?: "",
				detailLink = row[Prizes.detailLink] ?: "",
				laureates = laureates
			)
		}
	}

	override suspend fun getPrizeById(prizeId: Int): NobelPrize? = transaction {
		Prizes.select { Prizes.id eq prizeId }.firstOrNull()?.let { row ->
			val laureates = Laureates.select { Laureates.prizeId eq prizeId }.map { laureateRow ->
				Laureate(
					id = laureateRow[Laureates.id],
					prizeId = prizeId,
					fullName = laureateRow[Laureates.fullName],
					portion = laureateRow[Laureates.portion],
					motivation = laureateRow[Laureates.motivation],
					portraitUrl = laureateRow[Laureates.portraitUrl] ?: ""
				)
			}

			NobelPrize(
				id = prizeId,
				awardYear = row[Prizes.awardYear],
				category = row[Prizes.category],
				fullName = row[Prizes.fullName] ?: "",
				motivation = row[Prizes.motivation] ?: "",
				detailLink = row[Prizes.detailLink] ?: "",
				laureates = laureates
			)
		}
	}

	override suspend fun getUserByUsername(username: String): User? = transaction {
		Users.select { Users.username eq username }.map {
			User(
				id = it[Users.id],
				username = it[Users.username],
				role = it[Users.role]
			)
		}.firstOrNull()
	}

	override suspend fun verifyUserCredentials(username: String, password: String): User? = transaction {
		val userRow = Users.select { Users.username eq username }.firstOrNull()
		userRow?.let {
			if (DatabaseFactory.verifyPassword(password, it[Users.passwordHash])) {
				User(
					id = it[Users.id],
					username = it[Users.username],
					role = it[Users.role]
				)
			} else null
		}
	}

	override suspend fun getUserFavorites(userId: Int): List<NobelPrize> = transaction {
		val favoritePrizeIds = UserPrizes.select { UserPrizes.userId eq userId }
			.map { it[UserPrizes.prizeId] }

		Prizes.select { Prizes.id inList favoritePrizeIds }.map { row ->
			val prizeId = row[Prizes.id]
			val laureates = Laureates.select { Laureates.prizeId eq prizeId }.map { laureateRow ->
				Laureate(
					id = laureateRow[Laureates.id],
					prizeId = prizeId,
					fullName = laureateRow[Laureates.fullName],
					portion = laureateRow[Laureates.portion],
					motivation = laureateRow[Laureates.motivation],
					portraitUrl = laureateRow[Laureates.portraitUrl] ?: ""
				)
			}

			NobelPrize(
				id = prizeId,
				awardYear = row[Prizes.awardYear],
				category = row[Prizes.category],
				fullName = row[Prizes.fullName] ?: "",
				motivation = row[Prizes.motivation] ?: "",
				detailLink = row[Prizes.detailLink] ?: "",
				laureates = laureates
			)
		}
	}

	override suspend fun addUserFavorite(userId: Int, prizeId: Int): Boolean = transaction {
		val exists = UserPrizes.select {
			(UserPrizes.userId eq userId) and (UserPrizes.prizeId eq prizeId)
		}.any()

		if (!exists) {
			UserPrizes.insert {
				it[UserPrizes.userId] = userId
				it[UserPrizes.prizeId] = prizeId
				it[UserPrizes.addedAt] = Instant.now()
			}
			true
		} else false
	}

	override suspend fun removeUserFavorite(userId: Int, prizeId: Int): Boolean = transaction {
		UserPrizes.deleteWhere {
			(UserPrizes.userId eq userId) and (UserPrizes.prizeId eq prizeId)
		} > 0
	}

	override suspend fun savePrize(prize: NobelPrize): Int = transaction {
		Prizes.insert {
			it[awardYear] = prize.awardYear
			it[category] = prize.category
			it[fullName] = prize.fullName
			it[motivation] = prize.motivation
			it[detailLink] = prize.detailLink
		} get Prizes.id
	}
}