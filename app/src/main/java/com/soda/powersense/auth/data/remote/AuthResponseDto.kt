package com.soda.powersense.auth.data.remote

data class AuthResponseDto(
    val user: UserDto,
    val token: String,
    val refreshToken: String?
)
