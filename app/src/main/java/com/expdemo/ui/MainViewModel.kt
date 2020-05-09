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

package com.expdemo.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expdemo.R

/**
 * Created by Manish Patel on 5/9/2020.
 */
class MainViewModel : ViewModel() {

    enum class Screen {
        WITHOUTSINGLETONDEMO,
        WITHSINGLETONDEMO,
        COROUTINE1DEMO,
        COROUTINE2DEMO,
        RETROFITDEMO,
        FRAGMENTFACTORYDEMO,
        VIEWMODELSTATEDEMO,
        JETPACKPAGINGDEMO,
        VIEWBIDINGDEMO,
        MARSLISTDEMO,
        FRAGMENTSHARINGDATADEMO,
        SETTINGSPANEL,
        UNKNOWN
    }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _screen = MutableLiveData<Screen>()

    // The external immutable LiveData for the request status
    val screen: LiveData<Screen>
        get() = _screen

    fun setButtonScreen(view: View) {
        when (view.id) {
            R.id.button_1 -> _screen.value = Screen.WITHOUTSINGLETONDEMO
            R.id.button_2 -> _screen.value = Screen.WITHSINGLETONDEMO
            R.id.button_3 -> _screen.value = Screen.COROUTINE1DEMO
            R.id.button_4 -> _screen.value = Screen.RETROFITDEMO
            R.id.button_5 -> _screen.value = Screen.FRAGMENTFACTORYDEMO
            R.id.button_6 -> _screen.value = Screen.COROUTINE2DEMO
            R.id.button_7 -> _screen.value = Screen.VIEWMODELSTATEDEMO
            R.id.button_8 -> _screen.value = Screen.JETPACKPAGINGDEMO
            R.id.button_9 -> _screen.value = Screen.VIEWBIDINGDEMO
            R.id.button_10 -> _screen.value = Screen.MARSLISTDEMO
            R.id.button_11 -> _screen.value = Screen.FRAGMENTSHARINGDATADEMO
            R.id.button_12 -> _screen.value = Screen.SETTINGSPANEL
        }
    }

    fun reset() {
        _screen.value = Screen.UNKNOWN
    }
}