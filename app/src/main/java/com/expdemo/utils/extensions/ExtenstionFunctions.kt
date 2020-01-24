package com.expdemo.utils.extensions

import java.io.IOException
import com.expdemo.utils.state.Result

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> =
    try {
        call.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(IOException(e.localizedMessage, e))
    }

val <T> T.exhaustive: T get() = this