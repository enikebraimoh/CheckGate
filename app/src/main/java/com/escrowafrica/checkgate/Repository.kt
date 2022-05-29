package com.escrowafrica.checkgate

import com.escrowafrica.checkgate.ui.models.DepositData
import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
import com.escrowafrica.checkgate.ui.models.SignUpRequest
import com.escrowafrica.checkgate.ui.network.retrofit.CheckGateApis
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class Repository
@Inject
constructor(private val retrofitApis: CheckGateApis) {
    fun login(loginDetails: LoginRequest) = flow {
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


    fun signUp(loginDetails: SignUpRequest) = flow {
        emit(StateMachine.Loading)
        try {
            val response = retrofitApis.signUp(loginDetails)
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

    fun getBaskets() = flow {
        emit(StateMachine.Loading)
        try {
            val response = retrofitApis.getBaskets().data.baskets
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

    fun getBalance() = flow {
        emit(StateMachine.Loading)
        try {
            val response = retrofitApis.getWallet().data
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


    fun deposit(amount: DepositData) = flow {
        emit(StateMachine.Loading)
        try {
            val response = retrofitApis.deposit(amount)
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