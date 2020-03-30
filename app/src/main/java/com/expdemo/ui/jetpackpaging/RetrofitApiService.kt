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

package com.expdemo.ui.jetpackpaging

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Manish Patel on 3/30/2020.
 */
interface RetrofitApiService {

    @GET("/photos/")
    suspend fun getPopularImages(@Query("client_id") accessKey: String,
                                 @Query("page") page : Int,
                                 @Query("per_page") noPages : Int,
                                 @Query("order_by") orderBy : String) : Response<List<UnsplashImageDetails>>
}