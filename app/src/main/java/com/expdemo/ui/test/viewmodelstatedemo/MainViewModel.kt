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

package com.expdemo.ui.test.viewmodelstatedemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.expdemo.utils.constants.Constants
import timber.log.Timber

/**
 * Created by Manish Patel on 2/24/2020.
 */
class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val savedStateHandle = state

    var count = 0

    init {
        count = getSavedStateHandle()
        Timber.e("count: $count")
    }

    /** FUNCTIONS  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Set saved state handle */
    fun setSavedStateHandle() {
        // Sets a new value for the object associated to the key.
        Timber.e("saving count value : $count")
        savedStateHandle.set(Constants.COUNT_STATE, count)
    }

    /** Get saved state handle  */
    fun getSavedStateHandle(): Int {
        val count: Int? = savedStateHandle.get(Constants.COUNT_STATE)
        Timber.e("get count: $count")
        return savedStateHandle.get(Constants.COUNT_STATE) ?: 0
    }

    fun getUserName(): LiveData<String> {
        return savedStateHandle.getLiveData(Constants.USER_NAME)
    }

    fun setUserName(userName: String) {
        savedStateHandle.set(Constants.USER_NAME, userName)
    }

    override fun onCleared() {
        Timber.e("onCleared()")
        setSavedStateHandle()
        super.onCleared()
    }
}