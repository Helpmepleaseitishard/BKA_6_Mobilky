package com.example.KBA_6_2.presentation.list

import com.example.KBA_6_2.domain.entity.Laureate

sealed class LaureateListState {
    object Loading : LaureateListState()
    object Empty : LaureateListState()
    data class Success(val laureates: List<Laureate>) : LaureateListState()
    data class Error(val message: String) : LaureateListState()
}