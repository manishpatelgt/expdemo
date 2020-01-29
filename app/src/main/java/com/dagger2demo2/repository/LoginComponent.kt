package com.dagger2demo2.repository

import com.dagger2demo2.ui.MainActivity
import dagger.Subcomponent

/**
 * Created by Manish Patel on 1/29/2020.
 */
// @Subcomponent annotation informs Dagger this interface is a Dagger Subcomponent
@Subcomponent
interface LoginComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    // This tells Dagger that LoginActivity requests injection from LoginComponent
    // so that this subcomponent graph needs to satisfy all the dependencies of the
    // fields that LoginActivity is injecting
    fun inject(activity: MainActivity)
}