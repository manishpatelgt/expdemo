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

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.expdemo.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_common.*
import kotlinx.coroutines.delay

/**
 * Created by Manish Patel on 2/3/2020.
 */
/** https://www.javacodemonk.com/kotlin-coroutines-with-retrofit-and-livedata-790f6376 **/

class RetrofitDemoActivity : AppCompatActivity() {

    /** ViewModel */
    //private lateinit var mainViewModel: MainViewModel

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** Set observers*/
        setObservers()
    }

    fun setObservers() {

        if (mainViewModel.network) {
            getPersonProfileApi()
            lifecycleScope.launchWhenStarted() {
                delay(5000)
                getPersonProfileApi2()
            }
        } else {
            mainViewModel.setToastMessage(getString(R.string.no_internet_text))
        }

        /** Set observer for a toast message */
        val observerShowToast = Observer<String> {
            showToastMessage(it)
        }

        mainViewModel._showToast.observe(this, observerShowToast)

    }

    fun getPersonProfileApi() {
        /** 1st approch **/
        mainViewModel.loadData().observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    txt_view.text = "Loading data from network"
                }
                Status.SUCCESS -> {
                    val person = networkResource.data
                    person?.let {
                        txt_view.text =
                            person.firstName + " " + person.lastName + "\n" + person.email
                    }
                }
                Status.ERROR -> {
                    txt_view.text = "Error loading data from network"
                }
            }
        })
    }

    fun getPersonProfileApi2() {
        /** 2nd approch **/
        mainViewModel.loadData2.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    txt_view.text = "Loading data from network"
                }
                Status.SUCCESS -> {
                    val person = networkResource.data
                    person?.let {
                        txt_view.text =
                            person.firstName + " " + person.lastName + "\n" + person.email
                    }
                }
                Status.ERROR -> {
                    txt_view.text = "Error loading data from network"
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /** Show toast message */
    private fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }

}