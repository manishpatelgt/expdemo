package com.daggerdemo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggerdemo.application.App
import com.daggerdemo.models.Post
import com.daggerdemo.repository.UserRepository
import com.daggerdemo.utils.extensions.isNetworkAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.daggerdemo.utils.state.Result

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    /** Check network and cache conditions */
    val network = (isNetworkAvailable(App.context))
    private var postJob: Job? = null

    var isLoading = MutableLiveData<Boolean>()
    var apiError = MutableLiveData<String>()
    var postList: MutableLiveData<List<Post>> = MutableLiveData()

    /** Show a toast message */
    var _showToast = MutableLiveData<String>()

    init {
        //postList = MutableLiveData()
    }

    fun getPosts() {
        isLoading.value = true
        postJob = viewModelScope.launch {
            when (val result = userRepository.getPosts()) {
                is Result.Success -> {
                    isLoading.value = false
                    postList?.value = result.data

                    /** clear table and insert all the records **/
                    userRepository.deleteAll()

                    /** insert all records in one shot **/
                    userRepository.insertAll(result.data)
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