package com.escrowafrica.checkgate.ui.models

data class SignUpRequest(
    val confirmPassword: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String
)

data class SignUpResponse(
    val success: Boolean,
    val message: String,
)