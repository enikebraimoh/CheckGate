package com.escrowafrica.checkgate.ui.models

data class LoginRequest(val email : String, val password : String)

data class LoginResponse(
    val jwt: String,
    val message: String,
    val success: Boolean
)