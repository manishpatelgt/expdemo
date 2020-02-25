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

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.expdemo.R
import kotlinx.android.synthetic.main.activity_view_model_state_demo.*

/**
 * Created by Manish Patel on 2/24/2020.
 */
/**
 * https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
https://medium.com/androiddevelopers/viewmodels-persistence-onsaveinstancestate-restoring-ui-state-and-loaders-fc7cc4a6c090
https://proandroiddev.com/optimizing-viewmodel-with-lifecycle-2-2-0-a2895b5c01fd
https://blog.mindorks.com/viewmodel-with-savedstate
 */
class ViewModelStateDemoActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    //val mainViewModel by viewModels<MainViewModel>()

    /*private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }*/

    /*private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, SavedStateViewModelFactory(application, this)).get(MainViewModel::class.java)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_state_demo)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateText()

        count_button.setOnClickListener {
            mainViewModel.count = mainViewModel.count + 1
            mainViewModel.setSavedStateHandle()
            updateText()
        }

        submit_button.setOnClickListener {
            //save userName
            mainViewModel.setUserName(edit_username.text.toString())
        }

        //get last saved username  value and display
        val userNameObserver = Observer<String> { userName ->
            updateUserText(userName)
        }

        mainViewModel.getUserName().observe(this, userNameObserver)
    }

    fun updateText() {
        txt_count_view.text = "" + mainViewModel.count
    }

    fun updateUserText(userName: String){
        txt_username.text = "Last UserName: $userName"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}