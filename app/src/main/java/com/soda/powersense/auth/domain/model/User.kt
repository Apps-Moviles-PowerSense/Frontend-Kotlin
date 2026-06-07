package com.soda.powersense.auth.domain.model

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)
