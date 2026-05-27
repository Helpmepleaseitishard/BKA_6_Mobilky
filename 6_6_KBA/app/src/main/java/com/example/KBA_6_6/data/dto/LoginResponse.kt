// LoginResponse.kt
package com.example.KBA_6_6.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val token: String)