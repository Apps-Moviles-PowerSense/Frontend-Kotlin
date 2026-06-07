package com.soda.powersense.auth.data.remote

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String
)
