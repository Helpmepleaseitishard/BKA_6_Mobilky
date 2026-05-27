package com.example.KBA_6_1.domain.entity

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val downloadUrl: String,
    val thumbnailUrl: String
)