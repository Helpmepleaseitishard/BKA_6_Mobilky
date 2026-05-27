package com.example.KBA_6_1.domain.repository

import com.example.KBA_6_1.domain.entity.Photo

interface PhotoRepository {
    suspend fun getPhotos(): List<Photo>
}