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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.expdemo.R
import kotlinx.android.synthetic.main.activity_common.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Manish Patel on 2/18/2020.
 */
/** https://android.jlelse.eu/coroutine-in-android-working-with-lifecycle-fc9c1a31e5f3
https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-main-scope.html
https://discuss.kotlinlang.org/t/coroutines-on-activity/14369/7 **/

class CoroutineTestActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupUI()
    }

    fun setupUI() {

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

    companion object {
        val TAG = CoroutineTestActivity2::class.java.name
    }
}