package com.daggerandroidinjector.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daggerandroidinjector.utils.state.Result
import com.daggerandroidinjector.application.App
import com.daggerandroidinjector.model.Post
import com.daggerandroidinjector.repository.PostRepository
import com.daggerandroidinjector.utils.LiveEvent
import com.daggerandroidinjector.utils.isNetworkAvailable
import com.daggerandroidinjector.utils.toSingleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Manish Patel on 2/6/2020.
 */
class MainActivityViewModel @Inject constructor(
    private val postRepository: PostRepository,
    context: CoroutineContext
) : ViewModel() {

    /** Check network and cache conditions */
    val network = (isNetworkAvailable(App.context))

    private val job = Job()
    private val uiScope = CoroutineScope(context + job)

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean> = _showProgress

    private val _errorMessage = LiveEvent<String>()
    val errorMessage = _errorMessage.toSingleEvent()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    /** Show a toast message */
    var _showToast = MutableLiveData<String>()

    /** call Post API and return back list **/
    fun getPosts() {
        _showProgress.value = true
        viewModelScope.launch {
            when (val result = postRepository.getPosts()) {
                is Result.Success -> {

                    _showProgress.value = true

                    val rows = result.data
                    _posts.value = rows

                    /** clear table and insert all the records **/
                    postRepository.deleteAll()

                    /** insert all records in one shot **/
                    postRepository.insertAll(result.data)
                }
                is Result.Error -> {
                    _showProgress.value = false
                    _errorMessage.value = result.exception.message
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        launchAuthFlow {
            postRepository.signIn(email, password)
        }
    }

    private fun launchAuthFlow(block: suspend () -> Unit): Job {
        return uiScope.launch {
            try {
                _showProgress.value = true
                block()
            } catch (httpException: HttpException) {
                _errorMessage.value =
                    httpException.response()?.errorBody()?.string() ?: httpException.message()
            } catch (t: Throwable) {
                _errorMessage.value = t.message
            } finally {
                _showProgress.value = false
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