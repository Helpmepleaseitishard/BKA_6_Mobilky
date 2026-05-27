package com.example.KBA_6_2.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NobelResponseDto(
    @SerialName("nobelPrizes") val nobelPrizes: List<NobelPrizeDto> = emptyList()
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NobelPrizeDto(
    @SerialName("awardYear") val awardYear: String = "",
    @SerialName("category") val category: CategoryNameDto = CategoryNameDto("", ""),
    @SerialName("categoryFullName") val categoryFullName: CategoryNameDto? = null,
    @SerialName("laureates") val laureates: List<LaureateDto>? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CategoryNameDto(
    @SerialName("en") val en: String = "",
    @SerialName("no") val no: String = ""
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class LaureateDto(
    @SerialName("id") val id: String = "",
    @SerialName("fullName") val fullName: FullNameDto? = null,
    @SerialName("motivation") val motivation: MotivationDto? = null,
    @SerialName("birth") val birth: BirthDto? = null,  // ← опционально
    @SerialName("portraitUrl") val portraitUrl: String? = null  // ← опционально
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class FullNameDto(
    @SerialName("en") val en: String = ""
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MotivationDto(
    @SerialName("en") val en: String = ""
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BirthDto(
    @SerialName("place") val place: BirthPlaceDto? = null,
    @SerialName("country") val country: BirthCountryDto? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BirthPlaceDto(
    @SerialName("city") val city: String? = null,
    @SerialName("country") val country: BirthCountryDto? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BirthCountryDto(
    @SerialName("en") val en: String? = null
)