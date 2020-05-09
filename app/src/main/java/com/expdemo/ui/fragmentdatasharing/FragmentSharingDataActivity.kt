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

package com.expdemo.ui.fragmentdatasharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.R
import com.expdemo.databinding.ActivityFragmentDataSharingBinding
import com.expdemo.utils.constants.Constants

/**
 * Created by Manish Patel on 5/9/2020.
 */
class FragmentSharingDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentDataSharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentDataSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Set action bar */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val args = Bundle().apply {
            putString(Constants.USER_ID, "1000")
            putString(Constants.USER_NAME, "Manish")
        }
        val fragmentOne = FragmentOne()
        fragmentOne.arguments = args

        /** load fragment one in container **/
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView_1, fragmentOne)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}