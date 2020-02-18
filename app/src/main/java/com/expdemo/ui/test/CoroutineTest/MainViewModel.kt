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

package com.expdemo.ui.test.CoroutineTest

import androidx.lifecycle.*
import kotlinx.coroutines.delay

/**
 * Created by Manish Patel on 2/18/2020.
 */
class MainViewModel : ViewModel() {

    val mainRepository = MainRepository() // normally this would come from DI

    val intLiveData: LiveData<Int> = liveData {
        if (latestValue == null) {
            delay(3000)
            emit(mainRepository.getData())
        }
    }

    val strLiveData = liveData {
        val liveData = mainRepository.getStrLiveData()
        emitSource(liveData)

        delay(2000)
        val sLiveData = mainRepository.getAnotherStrLiveData()
        emitSource(sLiveData)
    }

    val mutableLiveData = MutableLiveData(5)

    val transEmitLiveData = mutableLiveData.switchMap { value ->
        liveData {
            val data = mainRepository.getValueFromParam(value)
            emit(data)
        }
    }

    val transEmitSourceLiveData = mutableLiveData.switchMap { value ->
        liveData {
            val liveData = mainRepository.getLiveDataFromParam(value)
            emitSource(liveData)
        }
    }

    /*val strLiveData = liveData {
        val liveData = mainRepository.getStrLiveData()
        emitSource(liveData)
    }*/

    class MainRepository {

        suspend fun getData(): Int {
            delay(1000)
            return 6
        }

        suspend fun getStrLiveData(): LiveData<String> {
            delay(1000)
            return MutableLiveData("LiveData")
        }

        suspend fun getAnotherStrLiveData(): LiveData<String> {
            delay(1000)
            return MutableLiveData("Second LiveDAta")
        }

        suspend fun getValueFromParam(id: Int):Int{
            delay(1000)
            return id
        }

        suspend fun getLiveDataFromParam(id: Int):LiveData<String>{
            delay(1000)
            return MutableLiveData("id: $id")
        }

    }
}