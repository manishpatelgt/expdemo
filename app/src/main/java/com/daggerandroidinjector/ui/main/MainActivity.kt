package com.daggerandroidinjector.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daggerandroidinjector.R
import dagger.android.AndroidInjection

/** https://medium.com/@serapbercin001/how-to-use-android-injector-for-activity-and-fragment-objects-through-new-dagger-2-with-kotlin-704eb8a98c43 **/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
    }
}