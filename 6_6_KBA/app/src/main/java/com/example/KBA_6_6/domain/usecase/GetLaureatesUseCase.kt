package com.example.KBA_6_6.domain.usecase

import com.example.KBA_6_6.domain.entity.Laureate
import com.example.KBA_6_6.domain.repository.NobelRepository

class GetLaureatesUseCase(
    private val repository: NobelRepository
) {
    suspend operator fun invoke(year: String?, category: String?): List<Laureate> {
        return repository.getLaureates(year, category)
    }
}