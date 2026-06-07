package com.soda.powersense.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val token: String,
    val refreshToken: String?
)
