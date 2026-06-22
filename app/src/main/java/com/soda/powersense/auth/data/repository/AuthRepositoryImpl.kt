package com.soda.powersense.auth.data.repository

import com.soda.powersense.alerts.data.local.AlertDao
import com.soda.powersense.auth.data.local.UserDao
import com.soda.powersense.auth.data.mapper.toDomain
import com.soda.powersense.auth.data.mapper.toEntity
import com.soda.powersense.auth.data.remote.AuthService
import com.soda.powersense.auth.data.remote.LoginRequestDto
import com.soda.powersense.auth.data.remote.RegisterRequestDto
import com.soda.powersense.auth.data.remote.UpdateProfileRequest
import com.soda.powersense.auth.domain.model.AuthResult
import com.soda.powersense.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val alertDao: AlertDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = authService.login(LoginRequestDto(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    alertDao.clearAlerts() // Clean previous alerts
                    userDao.upsert(body.toEntity())
                    Result.success(body.toDomain())
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<AuthResult> {
        return try {
            val response = authService.register(RegisterRequestDto(email, password, name))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    alertDao.clearAlerts() // Clean previous alerts
                    userDao.upsert(body.toEntity())
                    Result.success(body.toDomain())
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSession(): Flow<AuthResult?> {
        return userDao.getUser().map { it?.toDomain() }
    }

    override suspend fun updateName(newName: String): Result<Unit> {
        return try {
            val response = authService.updateProfile(UpdateProfileRequest(name = newName))
            if (response.isSuccessful) {
                val userDto = response.body()
                val currentEntity = userDao.getUser().firstOrNull()
                if (userDto != null && currentEntity != null) {
                    val updatedEntity = currentEntity.copy(
                        name = userDto.name,
                        updatedAt = userDto.updatedAt
                    )
                    userDao.upsert(updatedEntity)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Could not update local session"))
                }
            } else {
                Result.failure(Exception("Error updating name: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        alertDao.clearAlerts()
        userDao.clearUser()
    }
}
