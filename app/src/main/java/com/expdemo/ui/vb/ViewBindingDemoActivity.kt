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
import androidx.activity.viewModels
import com.expdemo.R
import com.expdemo.databinding.ViewDemoBinding

/**
 * Created by Manish Patel on 3/30/2020.
 */
/** https://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fchttps://medium.com/androiddevelopers/use-view-binding-to-replace-findviewbyid-c83942471fc
 * https://developer.android.com/topic/libraries/view-binding
 * https://medium.com/@.me./get-rid-of-fragment-activity-boilerplate-code-using-kotlin-1b103763baf8?_branch_match_id=560684694922163992
 */

class ViewBindingDemoActivity : BaseActivity<MainViewModel, ViewDemoBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        /** Set action bar */
        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {

            /** Load fragment -1 **/
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView_1, FirstFragment())
                .commit()

            /** Load fragment -2 **/
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView_2, SecondFragment())
                .commit()
        }
    }

    override fun getViewBinding(): ViewDemoBinding = ViewDemoBinding.inflate(layoutInflater)

    override val mViewModel: MainViewModel by viewModels()

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}