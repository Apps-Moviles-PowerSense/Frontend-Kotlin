package com.soda.powersense.auth.domain.model

data class AuthResult(
    val user: User,
    val token: String,
    val refreshToken: String?
)
