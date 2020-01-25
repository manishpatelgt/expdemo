package com.expdemo.repository

import com.expdemo.application.App

class UserLocalDataSource {

    lateinit var postDao: PostDao

    init {
        postDao = PostDatabase.getDatabase(App.context).postDao()
    }

}