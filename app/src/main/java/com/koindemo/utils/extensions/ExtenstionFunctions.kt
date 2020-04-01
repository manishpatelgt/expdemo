package com.koindemo.utils.extensions

import java.io.IOException
import com.koindemo.utils.state.Result
import retrofit2.HttpException
import java.net.SocketTimeoutException

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> =
    try {
        call.invoke()
    } catch (throwable: Exception) {
        throwable.printStackTrace()
        Result.Error(throwable.message.toString())
    }

val <T> T.exhaustive: T get() = this

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

fun handleException(e: Exception): Result.Error {
    return when (e) {
        is HttpException -> Result.Error(getErrorMessage(e.code()))
        is SocketTimeoutException -> Result.Error(getErrorMessage(ErrorCodes.SocketTimeOut.code))
        else -> Result.Error(getErrorMessage(Int.MAX_VALUE))
    }
}

private fun getErrorMessage(code: Int): String {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> "Timeout"
        401 -> "Unauthorised"
        404 -> "Not found"
        else -> "Something went wrong"
    }
}

