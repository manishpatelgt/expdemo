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

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.expdemo.R
import kotlinx.android.synthetic.main.activity_common.*
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by Manish Patel on 2/18/2020.
 */
/** https://android.jlelse.eu/coroutine-in-android-working-with-lifecycle-fc9c1a31e5f3
https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-main-scope.html
https://discuss.kotlinlang.org/t/coroutines-on-activity/14369/7 **/

class CoroutineTestActivity2 : AppCompatActivity() {

    /*private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }*/

    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** lifecycle scope **/
        //test1()

        /** Handling exception in lifecycle coroutines **/
        //test2()

        /** suspend life-cycle aware coroutines **/
        //test3()

        //test4()

        /** Set observers*/
        setObservers()
    }

    fun setObservers() {
        /** Set observer for a livedatae */
        val observerInt = Observer<Int> {
            Log.e(TAG, "Int: $it")
        }
        mainViewModel.intLiveData.observe(this, observerInt)

        /** Set observer for a livedatae */
        val observerStr = Observer<String> {
            Log.e(TAG, "Str: $it")
        }
        mainViewModel.strLiveData.observe(this, observerStr)

        mainViewModel.transEmitLiveData.observe(this, Observer {
            Log.e(TAG, "Value from liveData:: $it")
        })

        mainViewModel.transEmitSourceLiveData.observe(this, Observer {
            Log.e(TAG, "Value from emitSource:: $it")
        })
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart()")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop()")
    }

    fun test4() {
        lifecycleScope.launchWhenStarted {
            Log.e(TAG, "launchWhenStarted: before calling")
            val result = differentDispatcher()
            Log.e(TAG, "launchWhenStarted: after calling $result")
        }
    }

    suspend fun differentDispatcher(): Int = withContext(Dispatchers.Default) {
        for (i in 1..5) {
            delay(2000)
            Log.e(TAG, "inside different Dispatcher")
        }
        return@withContext 9
    }

    fun test3() {

        lifecycleScope.launchWhenStarted {
            while (true) {
                delay(2000)
                Log.e(TAG, "launchWhenStarted")
            }
        }
    }

    fun test2() {
        lifecycleScope.launch {

            var value = 0

            while (true) {
                delay(1000)
                Log.d(TAG, "From 1st coroutine: ${++value}")
            }
        }

        /*lifecycleScope.launch {

            launch(handler) {
                // This handler won't have any effect
                var value = 0

                while (true) {
                    delay(1000)
                    Log.d(TAG, "From 2nd coroutine: ${++value}")
                    throw NumberFormatException("Exception message")
                }
            }
        }*/

        lifecycleScope.launch(handler) {
            // This handler will handle exception
            var value = 0

            while (true) {
                delay(1000)
                Log.d(TAG, "From 2nd coroutine: ${++value}")
                throw NumberFormatException("Exception message")
            }
        }


        lifecycleScope.launch {

            /*val result: Deferred<Unit> = async {
                delay(3000)
                Log.d(TAG, "Throwing exception from async")
                throw ArithmeticException()
            }

            try {
                result.await()
            } catch (e: ArithmeticException) {
                Log.e(TAG, "Caught ArithmeticException")
            }*/
        }

    }

    fun errorTrial() {
        throw NumberFormatException("Exception message")
    }

    val handler = CoroutineExceptionHandler { context, throwable ->
        Log.e(TAG, "$throwable")
    }

    fun test1() {

        lifecycle.coroutineScope.launch() {
            Log.e(TAG, "Cool 1st coroutine started... Before delay")
            delay(3000)
            Log.e(TAG, "Cool 1st coroutine started... After delay")
        }

        lifecycleScope.launch {
            Log.e(TAG, "Cool 2nd coroutine started.... Before delay")
            delay(3000)
            Log.e(TAG, "Cool 2nd coroutine started... After delay")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        val TAG = CoroutineTestActivity2::class.java.name
    }
}