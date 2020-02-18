package com.expdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.expdemo.application.App
import com.expdemo.models.Post
import com.expdemo.repository.UserRepository
import com.expdemo.utils.extensions.isNetworkAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.expdemo.utils.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

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

    //https://medium.com/corouteam/exploring-kotlin-coroutines-and-lifecycle-architectural-components-integration-on-android-c63bb8a9156f
    //https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
    fun getPostsWithAwait() {
        viewModelScope.launch(Dispatchers.Main) {
            val posts: List<Post> = async(Dispatchers.IO) {
                userRepository.getPostsWithAwait()
            }.await()
            postList?.value = posts
        }
    }

    //https://www.javacodemonk.com/kotlin-coroutines-with-retrofit-and-livedata-790f6376
    val getPostsWithLiveData = liveData(Dispatchers.IO) {
        when (val result = userRepository.getPosts()) {
            is Result.Success -> {
                /** clear table and insert all the records **/
                userRepository.deleteAll()
                /** insert all records in one shot **/
                userRepository.insertAll(result.data)

                /** single source of truth **/
                val posts = userRepository.getPostFromDB()
                emitSource(posts)

                //emit(result.data)
            }
        }
    }

    val getPostFromDB = liveData(Dispatchers.IO) {
        val posts = userRepository.getPostFromDB()
        emitSource(posts)
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