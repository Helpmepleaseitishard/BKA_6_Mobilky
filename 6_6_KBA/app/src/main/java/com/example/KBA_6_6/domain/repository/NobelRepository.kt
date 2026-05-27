package com.example.KBA_6_6.domain.repository

import com.example.KBA_6_6.domain.entity.Laureate

interface NobelRepository {
    suspend fun getLaureates(year: String?, category: String?): List<Laureate>
}