package com.daggerandroidinjector.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.daggerandroidinjector.R
import com.daggerandroidinjector.utils.viewModelProvider
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/** https://medium.com/@serapbercin001/how-to-use-android-injector-for-activity-and-fragment-objects-through-new-dagger-2-with-kotlin-704eb8a98c43 **/

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private val model by lazy { viewModelProvider(modelFactory) as MainActivityViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
    }
}