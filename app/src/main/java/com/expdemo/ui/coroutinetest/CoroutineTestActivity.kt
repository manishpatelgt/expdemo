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

package com.expdemo.ui.coroutinetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.R
import com.expdemo.models.builderdsltest.Student
import com.expdemo.models.builderdsltest.StudentDSL.Companion.student
import kotlinx.android.synthetic.main.activity_common.*
import kotlinx.coroutines.*

/**
 * Created by Manish Patel on 1/27/2020.
 */
/** https://medium.com/mindful-engineering/fast-lane-to-coroutines-bce8388ed82b **/


class CoroutineTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        CoroutineScope(Dispatchers.Main).launch {
            println("Launch started")
            val user =
                userLogin() //called without blocking the thread and suspends until result is returned.
            val favoritedResult = getFavoriteItemList("1")
            println("Launch completed")
        }

        coroutinBuilder()

        /*generalDetails called from launch builder */
        CoroutineScope(Dispatchers.Main).launch {
            generalDetails()
        }

        awaitDemo()
    }

    /** https://medium.com/mindorks/builder-pattern-vs-kotlin-dsl-c3ebaca6bc3b **/
    fun builderPatternAndDSLDemo() {

        /**Builder Patten **/
        //To create Student object now we can use
        Student.Builder()
            .name("Alex")
            .standard(10)
            .rollNumber(720)
            .build()

        /** simple DSL **/
        //To create Student object now we can use
        student {
            name = "Alex"
            standard = 10
            rollNumber = 720
        }
    }

    fun awaitDemo() {
        CoroutineScope(Dispatchers.Main).launch {
            val user = userLogin(
                "test",
                "test"
            ) //called in background thread and suspends until result is returned.

            /*val favoritedResult = async(Dispatchers.IO) { getFavoriteItemList2("1") }
            val purchasedResult = async(Dispatchers.IO) { getPurchasedItemList("1") }
            favoritedResult.await()
            purchasedResult.await()*/

            val deferreds = listOf(     // fetch two docs at the same time
                async { getFavoriteItemList2("1") },  // async returns a result for the first doc
                async { getPurchasedItemList("2") }   // async returns a result for the second doc
            )
            deferreds.awaitAll()

            //val itemsToShow = favoritedResult.await() + purchasedResult.await()

            //val favoritedResult = getFavoriteItemList2("1")  //fetches favorite items using user object
            //val purchasedResult = getPurchasedItemList("1")  //fetches purchased items using user object

            //val itemsToShow = favoritedResult.await() + purchasedResult.await()
            //showDataInUi(itemsToShow) //once we receive the result of both functions we show it to UI.
        }
    }

    suspend fun userLogin(number: String, password: String) {
        withContext(Dispatchers.IO) {
            //fetch the user details object in background thread using IO dispatcher
        }
    }

    fun getFavoriteItemList2(userId: String) {
    }

    fun getPurchasedItemList(userId: String) {
    }

    suspend fun getFavoriteItemList(userId: String) {
        withContext(Dispatchers.IO) {
            // fetching favorited items from network
            println("Launch inside")
        }
    }

    suspend fun generalDetails() {
        withContext(Dispatchers.IO) {
            // fetching general details from network
            println("Launch general details")
        }
    }

    fun coroutinBuilder() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                runBlocking {
                    delay(20000) //delay for 20 seconds
                }
                userLogin() //userLogin will not execute until 20 seconds.
            }
        }

    }

    // fetching the user details in background thread using IO dispatcher
    suspend fun userLogin() {
        withContext(Dispatchers.IO) {
            //val user = login(number,password) // Network call
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}