package com.escrowafrica.checkgate.ui.network.retrofit


import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
import com.escrowafrica.checkgate.ui.models.SignUpRequest
import com.escrowafrica.checkgate.ui.models.SignUpResponse
import retrofit2.http.*

interface CheckGateApis {

    @POST("auth")
    suspend fun login(
        @Body loginDetails: LoginRequest
    ): LoginResponse

    @POST("users")
    suspend fun signUp(
        @Body signUpDetails: SignUpRequest
    ): SignUpResponse

}