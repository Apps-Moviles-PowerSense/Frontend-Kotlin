package com.soda.powersense.auth.data.remote

data class UserDto(
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)
