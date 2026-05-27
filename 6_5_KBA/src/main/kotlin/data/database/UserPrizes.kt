package com.example.data.database
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserPrizes : Table("user_prizes") {
	val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
	val prizeId = reference("prize_id", Prizes.id, onDelete = ReferenceOption.CASCADE)
	val addedAt = timestamp("added_at").default(Instant.now())

	override val primaryKey = PrimaryKey(userId, prizeId)
}