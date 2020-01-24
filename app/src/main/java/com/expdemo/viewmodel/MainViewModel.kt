package com.expdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expdemo.application.App
import com.expdemo.models.Post
import com.expdemo.repository.UserRepository
import com.expdemo.utils.extensions.isNetworkAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.expdemo.utils.state.Result

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    /** Check network and cache conditions */
    val network = (isNetworkAvailable(App.context))
    private var postJob: Job? = null

    var isLoading = MutableLiveData<Boolean>()
    var apiError = MutableLiveData<String>()
    private var postList: MutableLiveData<List<Post>>? = null

    /** Show a toast message */
    var _showToast = MutableLiveData<String>()

    fun getPosts() {
        isLoading.value = true
        postJob = viewModelScope.launch {
            when (val result = userRepository.getPosts()) {
                is Result.Success -> {
                    isLoading.value = false
                    postList?.value = result.data
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

    override fun onCleared() {
        super.onCleared()
    }
}