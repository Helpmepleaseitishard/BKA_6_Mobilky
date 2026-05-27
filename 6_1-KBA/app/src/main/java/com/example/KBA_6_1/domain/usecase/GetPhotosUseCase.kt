package com.example.KBA_6_1.domain.usecase

import com.example.KBA_6_1.domain.entity.Photo
import com.example.KBA_6_1.domain.repository.PhotoRepository

class GetPhotosUseCase(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(): List<Photo> = repository.getPhotos()
}