package com.example.KBA_6_2.domain.entity

data class Laureate(
    val id: String,
    val year: String,
    val category: String,
    val categoryFullName: String,
    val fullName: String,
    val motivation: String,
    val fullMotivation: String,
    val country: String,
    val portraitUrl: String?
)