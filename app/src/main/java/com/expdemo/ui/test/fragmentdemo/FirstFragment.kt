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

package com.expdemo.ui.test.fragmentdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.expdemo.R
import com.expdemo.data.RetrofitApiService
import com.expdemo.data.RetrofitFactory
import timber.log.Timber

/**
 * Created by Manish Patel on 2/13/2020.
 */
class FirstFragment constructor(val userId: String) : Fragment() {

    //private val customFragmentFactory = CustomFragmentFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //childFragmentManager.fragmentFactory = customFragmentFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        val userId = bundle?.getString("UserId")
        Timber.e("UserId: $userId")
        println("UserId: $userId")
    }

    companion object {
        fun newArgBundle(arg: String) = Bundle().apply {
            putString("UserId", arg)
        }
    }
}