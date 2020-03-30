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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.expdemo.ui.jetpackpaging.repository.PopularDataSource
import com.expdemo.ui.jetpackpaging.repository.PopularDataSourceFactory

/**
 * Created by Manish Patel on 3/30/2020.
 */
class MainViewModel : ViewModel() {

    var popularPagedList: LiveData<PagedList<UnsplashImageDetails>>? = null
    private var popularLiveDataSource: LiveData<PageKeyedDataSource<Int, UnsplashImageDetails>>? = null

    init {
        val popularDataSourceFactory = PopularDataSourceFactory(viewModelScope)

        popularLiveDataSource = popularDataSourceFactory.getPopularLiveDataSource()

        val configPop: PagedList.Config = (PagedList.Config.Builder()).setEnablePlaceholders(false).setPageSize(
            PopularDataSource(viewModelScope).PAGE_SIZE).build()

        popularPagedList = LivePagedListBuilder(popularDataSourceFactory, configPop).build()
    }
}