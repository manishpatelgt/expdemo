package com.daggerdemo.repository

import com.expdemo.repository.UserLocalDataSource
import javax.inject.Inject

/**
 * Created by Manish Patel on 1/28/2020.
 */
// @Inject lets Dagger know how to create instances of this object

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {
}