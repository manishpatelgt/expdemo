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

package com.expdemo.ui.retrofitdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.expdemo.application.App
import com.expdemo.utils.extensions.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

/**
 * Created by Manish Patel on 2/3/2020.
 */
class MainViewModel : ViewModel() {

    /** Show a toast message */
    var _showToast = MutableLiveData<String>()

    /** Check network and cache conditions */
    val network = (isNetworkAvailable(App.context))
    val responseHandler by lazy { ResponseHandler() }
    val api = ApiClient.createService(EmployeeApi::class.java)

    fun loadData() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val response = api.getPerson()
        if (response.isSuccessful) {
            emit(Resource.success(response.body()?.data))
        } else {
            emit(Resource.error(response.message()))
        }
    }

    val loadData2 = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        emit(getData())
    }

    suspend fun getData(): Resource<Person> {
        return try {
            val response = api.getPerson()
            responseHandler.handleSuccess(response.body()?.data)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    /**
     * Show message in toast
     * */
    fun setToastMessage(message: String) {
        _showToast.value = message
    }
}