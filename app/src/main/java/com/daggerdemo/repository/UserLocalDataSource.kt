package com.expdemo.repository

import com.daggerdemo.application.App
import com.daggerdemo.repository.PostDao
import com.daggerdemo.repository.PostDatabase
import javax.inject.Inject

class UserLocalDataSource @Inject constructor() {

    lateinit var postDao: PostDao

    init {
        postDao = PostDatabase.getDatabase(App.context).postDao()
    }

}