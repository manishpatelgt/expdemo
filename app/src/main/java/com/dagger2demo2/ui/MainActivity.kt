package com.dagger2demo2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dagger2demo2.R
import com.dagger2demo2.application.App
import com.dagger2demo2.models.LoginViewModel
import com.dagger2demo2.repository.LoginComponent
import javax.inject.Inject

/** https://developer.android.com/training/dependency-injection/dagger-android **/

class MainActivity : AppCompatActivity() {

    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject
    lateinit var loginViewModel: LoginViewModel

    // Reference to the Login graph
    lateinit var loginComponent: LoginComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        /*// Make Dagger instantiate @Inject fields in MainActivity
        (applicationContext as App).appComponent.inject(this)
        // Now loginViewModel is available*/

        // Creation of the login graph using the application graph
        loginComponent = (applicationContext as App).appComponent.loginComponent().create()

        // Make Dagger instantiate @Inject fields in LoginActivity
        loginComponent.inject(this)

        // Now loginViewModel is available
        loginViewModel.getPosts()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}