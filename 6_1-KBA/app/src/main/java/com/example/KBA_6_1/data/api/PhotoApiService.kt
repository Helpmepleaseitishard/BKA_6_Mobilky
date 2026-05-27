package com.example.KBA_6_1.data.api

import com.example.KBA_6_1.data.dto.PhotoDto
import retrofit2.http.GET

interface PhotoApiService {
    @GET("v2/list?page=1&limit=50")
    suspend fun getPhotos(): List<PhotoDto>
}