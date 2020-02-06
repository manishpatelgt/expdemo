package com.daggerandroidinjector.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggerandroidinjector.utils.state.Result
import com.daggerandroidinjector.application.App
import com.daggerandroidinjector.model.Post
import com.daggerandroidinjector.repository.PostRepository
import com.daggerandroidinjector.utils.isNetworkAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Manish Patel on 2/6/2020.
 */
class MainActivityViewModel @Inject constructor(private val postRepository: PostRepository) :
    ViewModel() {

    /** Check network and cache conditions */
    val network = (isNetworkAvailable(App.context))
    private var postJob: Job? = null

    var isLoading = MutableLiveData<Boolean>()
    var apiError = MutableLiveData<String>()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    /** Show a toast message */
    var _showToast = MutableLiveData<String>()

    fun getPosts() {
        isLoading.value = true
        postJob = viewModelScope.launch {
            when (val result = postRepository.getPosts()) {
                is Result.Success -> {
                    isLoading.value = false

                    val rows = result.data
                    _posts.value = rows

                    /** clear table and insert all the records **/
                    postRepository.deleteAll()

                    /** insert all records in one shot **/
                    postRepository.insertAll(result.data)
                }
                is Result.Error -> {
                    apiError.value = result.exception.message
                    isLoading.value = false
                }
            }
        }
    }


    /**
     * Show message in toast
     * */
    fun setToastMessage(message: String) {
        _showToast.value = message
    }
}