package com.escrowafrica.checkgate

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.net.SocketTimeoutException

sealed class StateMachine<out T> {
    data class Success<out R>(val data: R) : StateMachine<R>()
    data class Error<R>(val error: Exception) : StateMachine<R>()
    data class TimeOut<R>(val error: SocketTimeoutException) : StateMachine<R>()
    data class GenericError(
        val code: Int? = null,
        val error: ErrorResponse? = null ) : StateMachine<Nothing>()
    object Ideal : StateMachine<Nothing>()
    object Loading : StateMachine<Nothing>()
}

data class ErrorResponse(
    @Expose
    val statusCode: String?,
    @Expose
    val message: String?
)

val ErrorResponse.message: String
    get() = message ?: kotlin.run { "An error occurred" }

fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.charStream()?.let {
            val type = object : TypeToken<ErrorResponse>() {}.type
            Gson().fromJson(it, type)
        }
    } catch (exception: Exception) {
        null
    }
}
