package com.koindemo.utils.extensions

import java.io.IOException
import com.koindemo.utils.state.Result

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> =
    try {
        call.invoke()
    } catch (throwable: Exception) {
        throwable.printStackTrace()
        /*when (throwable) {
            is IOException -> Result.NetworkError
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable)
                Result.GenericError(code, errorResponse)
            }
            else -> {
                Result.GenericError(null, null)
            }
        }*/
        Result.Error(IOException(throwable.localizedMessage, throwable))
    }

val <T> T.exhaustive: T get() = this
