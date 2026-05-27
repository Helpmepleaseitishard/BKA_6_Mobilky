// LoginRequest.kt
package com.example.KBA_6_6.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)