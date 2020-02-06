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
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            when (response.code()) {
                404 -> Result.Error(Exception("Webservice Not found"))
                401 -> Result.Error(Exception("Un-authorised"))
                500 -> Result.Error(Exception("Server not found"))
                else -> Result.Error(Exception("couldn't connect to server"))
            }
        }
    }

    suspend fun callPostApi2(): Result<List<Post>> {
        return try {
            val response = api.getPosts()
            println("status code: ${response.code()}")
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                when (response.code()) {
                    404 -> Result.Error(Exception("Webservice Not found"))
                    401 -> Result.Error(Exception("Un-authorised"))
                    500 -> Result.Error(Exception("Server not found"))
                    else -> Result.Error(Exception("couldn't connect to server"))
                }
            }

        } catch (httpException: HttpException) {
            println("error: ${httpException.message}")
            Result.Error(
                    Exception(
                            httpException.response()?.errorBody()?.string()
                                    ?: httpException.message(),
                            httpException
                    )
            )
        } catch (t: Throwable) {
            t.printStackTrace()
            println("error: ${t.message}")
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