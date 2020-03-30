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

package com.expdemo.ui.jetpackpaging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.expdemo.R
import kotlinx.android.synthetic.main.activity_jetpack_paging.*
import timber.log.Timber

/**
 * Created by Manish Patel on 3/30/2020.
 */
/** https://developer.android.com/topic/libraries/architecture/paging#paged-list
 * https://developer.android.com/topic/libraries/architecture/paging/data#custom-data-source
 */

class PagingActivity : AppCompatActivity(), MainAdapter.OnImageListener {

    val mainViewModel by viewModels<MainViewModel>()

    private lateinit var viewAdapter: MainAdapter
    private lateinit var viewManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_paging)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** setup UI **/
        setupUI()
    }

    private fun setupUI() {
        mainViewModel.popularPagedList?.observe(this, Observer { postList ->
            viewAdapter.submitList(postList)
        })
        viewManager = GridLayoutManager(this, 2)
        viewAdapter = MainAdapter(this)
        recyclerViewPopular.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onImageClick(position: Int) {
        Timber.e("Clicked: $position")
    }
}