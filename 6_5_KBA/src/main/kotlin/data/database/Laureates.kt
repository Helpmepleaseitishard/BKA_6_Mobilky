package com.example.data.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Laureates : Table("laureates") {
	val id = integer("id").autoIncrement()
	val prizeId = reference("prize_id", Prizes.id, onDelete = ReferenceOption.CASCADE)
	val fullName = varchar("full_name", 300)
	val portion = varchar("portion", 50)
	val motivation = text("motivation")
	val portraitUrl = varchar("portrait_url", 500).nullable()

	override val primaryKey = PrimaryKey(id)
}