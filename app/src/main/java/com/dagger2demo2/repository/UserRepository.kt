package com.dagger2demo2.repository

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Manish Patel on 1/28/2020.
 */
@Singleton
class UserRepository @Inject constructor(private val localDataSource: UserLocalDataSource,
                                         private val remoteDataSource: UserRemoteDataSource) {
}