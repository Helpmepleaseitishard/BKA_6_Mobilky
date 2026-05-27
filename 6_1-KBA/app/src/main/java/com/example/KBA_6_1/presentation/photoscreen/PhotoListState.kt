package com.example.a6_1.presentation.photoscreen

import com.example.KBA_6_1.domain.entity.Photo

sealed class PhotoListState {
    object Loading : PhotoListState()
    data class Success(val photos: List<Photo>) : PhotoListState()
    data class Error(val message: String) : PhotoListState()
}