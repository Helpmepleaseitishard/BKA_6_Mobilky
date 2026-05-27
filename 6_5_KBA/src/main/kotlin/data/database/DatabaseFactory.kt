package com.example.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

object DatabaseFactory {
	fun init() {
		// Используем H2 в режиме совместимости с PostgreSQL
		val config = HikariConfig().apply {
			driverClassName = "org.h2.Driver"
			jdbcUrl = "jdbc:h2:file:./data/nobelprizes;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE"
			username = "sa"
			password = ""
			maximumPoolSize = 5
		}

		Database.connect(HikariDataSource(config))

		transaction {
			// Создаём таблицы
			SchemaUtils.createMissingTablesAndColumns(
				Users, Prizes, Laureates, UserPrizes
			)

			// Создаём админа
			if (Users.select { Users.username eq "admin" }.empty()) {
				val hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt())
				Users.insert {
					it[username] = "admin"
					it[passwordHash] = hashedPassword
					it[role] = "admin"
				}
			}

			// Добавляем тестовые данные если таблица prizes пуста
			if (Prizes.selectAll().empty()) {
				// Вставляем премии
				val prize1 = Prizes.insert {
					it[awardYear] = 2023
					it[category] = "physics"
					it[fullName] = ""
					it[motivation] = "for experimental methods that generate attosecond pulses of light"
					it[detailLink] = "https://www.nobelprize.org/prizes/physics/2023/summary/"
				} get Prizes.id

				val prize2 = Prizes.insert {
					it[awardYear] = 2023
					it[category] = "peace"
					it[fullName] = ""
					it[motivation] = "for her fight against the oppression of women in Iran"
					it[detailLink] = "https://www.nobelprize.org/prizes/peace/2023/summary/"
				} get Prizes.id

				val prize3 = Prizes.insert {
					it[awardYear] = 2022
					it[category] = "literature"
					it[fullName] = ""
					it[motivation] = "for the courage and clinical acuity of her work"
					it[detailLink] = "https://www.nobelprize.org/prizes/literature/2022/summary/"
				} get Prizes.id

				// Вставляем лауреатов
				Laureates.insert {
					it[prizeId] = prize1
					it[fullName] = "Pierre Agostini"
					it[portion] = "1/3"
					it[motivation] = "for experimental methods that generate attosecond pulses"
					it[portraitUrl] = ""
				}

				Laureates.insert {
					it[prizeId] = prize1
					it[fullName] = "Ferenc Krausz"
					it[portion] = "1/3"
					it[motivation] = "for experimental methods that generate attosecond pulses"
					it[portraitUrl] = ""
				}

				Laureates.insert {
					it[prizeId] = prize1
					it[fullName] = "Anne L'Huillier"
					it[portion] = "1/3"
					it[motivation] = "for experimental methods that generate attosecond pulses"
					it[portraitUrl] = ""
				}

				Laureates.insert {
					it[prizeId] = prize2
					it[fullName] = "Narges Mohammadi"
					it[portion] = "1"
					it[motivation] = "for her fight against the oppression of women in Iran"
					it[portraitUrl] = ""
				}

				Laureates.insert {
					it[prizeId] = prize3
					it[fullName] = "Annie Ernaux"
					it[portion] = "1"
					it[motivation] = "for the courage and clinical acuity"
					it[portraitUrl] = ""
				}
			}
		}
	}

	fun verifyPassword(password: String, hash: String): Boolean {
		return BCrypt.checkpw(password, hash)
	}
}