package com.escrowafrica.checkgate

import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
import com.escrowafrica.checkgate.ui.network.retrofit.CheckGateApis
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class Repository
@Inject
constructor(private val retrofitApis: CheckGateApis) {
    fun login(loginDetails: LoginRequest) = flow<StateMachine<LoginResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = retrofitApis.login(loginDetails)
            emit(StateMachine.Success(response))
        } catch (e: Throwable) {
            when (e) {
                is IOException -> emit(StateMachine.Error(e))
                is HttpException -> {
                    val status = e.code()
                    val res = convertErrorBody(e)
                    emit(StateMachine.GenericError(status, res))
                }
                is SocketTimeoutException -> emit(StateMachine.TimeOut(e))
            }
        }

    }


}