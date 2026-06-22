package com.soda.powersense.auth.presentation.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
