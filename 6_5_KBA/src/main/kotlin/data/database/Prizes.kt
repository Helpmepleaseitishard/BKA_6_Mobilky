package com.example.data.database

import org.jetbrains.exposed.sql.Table

object Prizes : Table("prizes") {
	val id = integer("id").autoIncrement()
	val awardYear = integer("award_year")
	val category = varchar("category", 100)
	val fullName = varchar("full_name", 500).nullable()
	val motivation = text("motivation").nullable()
	val detailLink = varchar("detail_link", 500).nullable()

	override val primaryKey = PrimaryKey(id)
	init {
		uniqueIndex("idx_prize_year_category", awardYear, category)
	}
}