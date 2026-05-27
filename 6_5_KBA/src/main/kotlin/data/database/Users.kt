package com.example.data.database

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
	val id = integer("id").autoIncrement()
	val username = varchar("username", 100).uniqueIndex()
	val passwordHash = varchar("password_hash", 255)
	val role = varchar("role", 50).default("user")

	override val primaryKey = PrimaryKey(id)
}