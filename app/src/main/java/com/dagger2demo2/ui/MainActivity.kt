package com.dagger2demo2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dagger2demo2.R
import com.dagger2demo2.application.App
import com.dagger2demo2.models.LoginViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    // You want Dagger to provide an instance of LoginViewModel from the graph
    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make Dagger instantiate @Inject fields in LoginActivity
        (applicationContext as App).appComponent.inject(this)
        // Now loginViewModel is available

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}