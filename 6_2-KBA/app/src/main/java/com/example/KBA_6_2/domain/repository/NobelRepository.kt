package com.example.KBA_6_2.domain.repository

import com.example.KBA_6_2.domain.entity.Laureate

interface NobelRepository {
    suspend fun getLaureates(year: String?, category: String?): List<Laureate>
}