package com.expdemo.utils.extensions

import java.io.IOException
import com.expdemo.utils.state.Result
import com.squareup.moshi.Moshi
import retrofit2.HttpException

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
        Result.Error(throwable.message.toString())
        //Result.Error(IOException(throwable.localizedMessage, throwable))
    }

val <T> T.exhaustive: T get() = this

/*private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}*/