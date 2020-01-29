package com.dagger2demo2.models

import com.dagger2demo2.repository.UserRepository
import javax.inject.Inject

/**
 * Created by Manish Patel on 1/28/2020.
 */
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getPosts() {
        userRepository.getPosts()
    }
}