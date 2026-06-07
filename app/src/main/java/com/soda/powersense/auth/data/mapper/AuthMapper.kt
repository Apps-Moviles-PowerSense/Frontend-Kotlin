package com.soda.powersense.auth.data.mapper

import com.soda.powersense.auth.data.local.UserEntity
import com.soda.powersense.auth.data.remote.AuthResponseDto
import com.soda.powersense.auth.data.remote.UserDto
import com.soda.powersense.auth.domain.model.AuthResult
import com.soda.powersense.auth.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        avatarUrl = avatarUrl,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun AuthResponseDto.toDomain(): AuthResult {
    return AuthResult(
        user = user.toDomain(),
        token = token,
        refreshToken = refreshToken
    )
}

fun AuthResponseDto.toEntity(): UserEntity {
    return UserEntity(
        id = user.id,
        email = user.email,
        name = user.name,
        avatarUrl = user.avatarUrl,
        isActive = user.isActive,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
        token = token,
        refreshToken = refreshToken
    )
}

fun UserEntity.toDomain(): AuthResult {
    return AuthResult(
        user = User(
            id = id,
            email = email,
            name = name,
            avatarUrl = avatarUrl,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        ),
        token = token,
        refreshToken = refreshToken
    )
}
