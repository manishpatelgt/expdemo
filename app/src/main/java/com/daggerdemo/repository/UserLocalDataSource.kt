package com.daggerdemo.repository

import com.daggerdemo.application.App
import javax.inject.Inject

class UserLocalDataSource @Inject constructor() {

    lateinit var postDao: PostDao

    init {
        postDao = PostDatabase.getDatabase(App.context).postDao()
    }

}