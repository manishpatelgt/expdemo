package com.daggerandroidinjector.utils

import com.daggerandroidinjector.utils.state.Result
import retrofit2.HttpException
import java.lang.Exception

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        call.invoke()
    } catch (httpException: HttpException) {
        httpException.printStackTrace()
        Result.Error(
            Exception(
                httpException.response()?.errorBody()?.string() ?: httpException.message(),
                httpException
            )
        )
    } catch (t: Throwable) {
        t.printStackTrace()
        Result.Error(Exception(t.localizedMessage, t))
    }
}


