/*
 * Copyright 2020 Manish Patel. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expdemo.ui.jetpackpaging.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.expdemo.ui.jetpackpaging.RetrofitApiService
import com.expdemo.ui.jetpackpaging.RetrofitService
import com.expdemo.ui.jetpackpaging.UnsplashImageDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Created by Manish Patel on 3/30/2020.
 */
class PopularDataSource(private  val scope: CoroutineScope):  PageKeyedDataSource<Int, UnsplashImageDetails>() {

    val PAGE_SIZE = 20
    val FIRST_PAGE = 1
    val accessKey = "ccd043c45adadef3d68d700600890401a9d0a97f15fae3bfb9665d2b4f2d5593"
    val orderBy = "popular"
    var retrofitApiService: RetrofitApiService = RetrofitService.createService(RetrofitApiService::class.java)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            try {
                val response = retrofitApiService.getPopularImages(accessKey, FIRST_PAGE, PAGE_SIZE, orderBy)
                when {
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                    }
                }
            } catch (exception: Exception) {
                Log.e("repository->Posts", "" + exception.message)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            try {
                val response = retrofitApiService.getPopularImages(accessKey, params.key, PAGE_SIZE, orderBy)
                when {
                    response.isSuccessful -> {
                        val key: Int?
                        if (response.body()?.isNotEmpty()!!) key = params.key + 1
                        else key = null
                        callback.onResult(response.body()!!, key)
                    }
                }
            } catch (exception: Exception) {
                Log.e("repository->Popular", "" + exception.message)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            try {
                val response = retrofitApiService.getPopularImages(accessKey, params.key, PAGE_SIZE, orderBy)
                val key: Int?
                if (params.key > 1) key = params.key - 1
                else key = null
                when {
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!, key)
                    }
                }
            } catch (exception: Exception) {
                Log.e("repository->Popular", "" + exception.message)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}