package com.expdemo.application

import android.content.Context
import com.expdemo.repository.UserLocalDataSource
import com.expdemo.repository.UserRemoteDataSource
import com.expdemo.repository.UserRepository

// Container of objects shared across the whole app
class AppContainer() {

    // Since you want to expose userRepository out of the container, you need to satisfy
    // its dependencies as you did before
    val remoteDataSource = UserRemoteDataSource()
    val localDataSource = UserLocalDataSource()

    // userRepository is not private; it'll be exposed
    val userRepository = UserRepository(localDataSource, remoteDataSource)
}