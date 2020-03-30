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

package com.expdemo.ui.vb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.databinding.ViewDemoBinding

/**
 * Created by Manish Patel on 3/30/2020.
 */
/** https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fchttps://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
 * https://developer.android.com/topic/libraries/view-binding
 * https://medium.com/@.me./get-rid-of-fragment-activity-boilerplate-code-using-kotlin-1b103763baf8?_branch_match_id=560684694922163992
 */

class ViewBindingDemoActivity : AppCompatActivity() {

    private lateinit var binding: ViewDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewDemoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /** Set action bar */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}