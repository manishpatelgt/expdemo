package com.dagger2demo2.application

import com.dagger2demo2.repository.LoginComponent
import com.dagger2demo2.repository.NetworkModule
import com.dagger2demo2.repository.SubcomponentsModule
import com.dagger2demo2.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Manish Patel on 1/28/2020.
 */
// The "modules" attribute in the @Component annotation tells Dagger what Modules
// to include when building the graph

// Including SubcomponentsModule, tell ApplicationComponent that
// LoginComponent is its subcomponent.

@Singleton
@Component(modules = [NetworkModule::class, SubcomponentsModule::class])
interface ApplicationComponent {

    /*// This tells Dagger that MainActivity requests injection so the graph needs to
    // satisfy all the dependencies of the fields that LoginActivity is requesting.
    fun inject(activity: MainActivity)*/

    // This function exposes the LoginComponent Factory out of the graph so consumers
   // can use it to obtain new instances of LoginComponent
    fun loginComponent(): LoginComponent.Factory
}