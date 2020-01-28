package com.daggerdemo.repository

import dagger.Component
import javax.inject.Singleton

/**
 * Created by Manish Patel on 1/28/2020.
 */
// @Component makes Dagger create a graph of dependencies
@Singleton
@Component
interface ApplicationGraph {

    // The return type  of functions inside the component interface is
    // what can be provided from the container
    fun repository(): UserRepository
}