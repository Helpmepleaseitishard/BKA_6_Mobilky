package com.example.KBA_6_1.data.repository

import com.example.KBA_6_1.data.api.PhotoApiService
import com.example.KBA_6_1.data.di.NetworkModule
import com.example.KBA_6_1.domain.entity.Photo
import com.example.KBA_6_1.domain.repository.PhotoRepository

class PhotoRepositoryImpl : PhotoRepository {

    private val api: PhotoApiService = NetworkModule.photoApiService

    override suspend fun getPhotos(): List<Photo> {
        return api.getPhotos().map { dto ->
            Photo(
                id = dto.id,
                author = dto.author,
                width = dto.width,
                height = dto.height,
                downloadUrl = dto.downloadUrl,
                thumbnailUrl = "https://picsum.photos/id/${dto.id}/200/200"
            )
        }
    }
}