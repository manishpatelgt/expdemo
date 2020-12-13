package com.daggerhiltdemo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggerhiltdemo.data.model.Post
import com.daggerhiltdemo.data.repository.MainRepository
import com.daggerhiltdemo.utils.NetworkHelper
import com.daggerhiltdemo.utils.Resource
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _posts = MutableLiveData<Resource<List<Post>>>()
    val posts: LiveData<Resource<List<Post>>>
        get() = _posts

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _posts.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getPosts().let {
                    if (it.isSuccessful) {
                        _posts.postValue(Resource.success(it.body()))
                    } else _posts.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _posts.postValue(Resource.error("No internet connection", null))
        }
    }

}