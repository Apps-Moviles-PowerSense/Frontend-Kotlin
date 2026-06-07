package com.soda.powersense.auth.domain.repository

import com.soda.powersense.auth.domain.model.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun register(email: String, password: String, name: String): Result<AuthResult>
    fun getSession(): Flow<AuthResult?>
    suspend fun logout()
}
