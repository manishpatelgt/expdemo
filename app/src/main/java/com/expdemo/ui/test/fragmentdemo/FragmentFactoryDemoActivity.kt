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
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.R
import kotlinx.android.synthetic.main.activity_common.*

/**
 * Created by Manish Patel on 2/13/2020.
 */
/*
 * https://proandroiddev.com/android-fragments-fragmentfactory-ceec3cf7c959
 * https://proandroiddev.com/android-fragments-fragmentcontainerview-292f393f9ccf
 * https://developer.android.com/jetpack/androidx/releases/fragment
 */

class FragmentFactoryDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_factory)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView_1, FirstFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}