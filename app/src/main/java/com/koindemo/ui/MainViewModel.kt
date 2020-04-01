package com.koindemo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.koindemo.model.Post
import com.koindemo.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import com.koindemo.utils.state.Result
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Manish Patel on 4/1/2020.
 */
class MainViewModel constructor(private val postRepository: PostRepository) : ViewModel(){

    //https://www.javacodemonk.com/kotlin-coroutines-with-retrofit-and-livedata-790f6376
    val getPostsWithLiveData = liveData(Dispatchers.IO) {

        when (val result = postRepository.getPosts()) {
            is Result.Success -> {

                /** clear table and insert all the records **/
                postRepository.deleteAll()
                /** insert all records in one shot **/
                postRepository.insertAll(result.data!!)

                /** single source of truth **/
                val posts = postRepository.getPostFromDB()
                emitSource(posts)

                //emit(result.data)
            }
            is Result.Error -> {
                Timber.e("Error: ${result.message.toString()}")
                //emitSource(result.message.toString())
            }
        }
    }

    var postList: MutableLiveData<List<Post>> = MutableLiveData()

    fun getPosts() {
         viewModelScope.launch {
            when (val result = postRepository.getPosts()) {
                is Result.Success -> {
                    postList?.value = result.data

                    /** clear table and insert all the records **/
                    postRepository.deleteAll()

                    /** insert all records in one shot **/
                    postRepository.insertAll(result.data!!)
                }
                is Result.Error -> {
                    Timber.e("Error: ${result.message.toString()}")
                }
            }
        }
    }
}