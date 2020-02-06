package com.daggerandroidinjector.repository

import com.daggerandroidinjector.api.PostApi
import com.daggerandroidinjector.model.Post
import com.daggerandroidinjector.utils.safeApiCall
import java.io.IOException
import javax.inject.Inject
import com.daggerandroidinjector.utils.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import javax.inject.Singleton

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Singleton
class PostRepository @Inject constructor(
    private val api: PostApi,
    private val postDao: PostDao
) {

    suspend fun getPosts() = safeApiCall(
        call = { callPostApi() }
    )

    suspend fun callPostApi(): Result<List<Post>> {
        val response = api.getPosts()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(Exception(response.message()))
    }

    suspend fun callPostApi2(): Result<List<Post>> {
        return try {
            val response = api.getPosts()
            Timber.e("status code: ${response.code()}")
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Timber.e("error: ${response.message()}")
                Result.Error(Exception(response.message()))
            }

        } catch (httpException: HttpException) {
           Timber.e("error: ${httpException.message}")
            Result.Error(
                Exception(
                    httpException.response()?.errorBody()?.string() ?: httpException.message(),
                    httpException
                )
            )
        } catch (t: Throwable) {
            t.printStackTrace()
            Timber.e("error: ${t.message}")
            Result.Error(Exception(t.localizedMessage, t))
        }
    }

    suspend fun signIn(email: String, password: String) {
        withContext(Dispatchers.IO) {
            //val tokenResponse = api.getToken(getBasicAuthHeader(email, password))
        }
    }

    suspend fun insert(post: Post) {
        postDao.insert(post)
    }

    suspend fun insertAll(posts: List<Post>) {
        postDao.insertAll(posts)
    }

    suspend fun deleteAll() {
        postDao.deleteAll()
    }
}