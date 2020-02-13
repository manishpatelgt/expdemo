package com.expdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.R
import com.expdemo.ui.retrofitdemo.RetrofitDemoActivity
import com.expdemo.ui.test.CoroutineTestActivity
import com.expdemo.ui.test.fragmentdemo.FragmentFactoryDemoActivity
import com.expdemo.ui.withoutsingelton.WithoutSingletonActivity
import com.expdemo.ui.withsingelton.WithSingletonActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Set action bar */
        setSupportActionBar(toolbar)

        without_singleton_button.setOnClickListener {
            /** lunch without singleton activity**/
            startActivity<WithoutSingletonActivity>()
        }

        with_singleton_button.setOnClickListener {
            /** lunch with singleton activity**/
            startActivity<WithSingletonActivity>()
        }

        coroutine_test_button.setOnClickListener {
            /** lunch with CoroutineTestActivity**/
            startActivity<CoroutineTestActivity>()
        }

        retrofit_generic_button.setOnClickListener {
            /** lunch with RetrofitDemoActivity**/
            startActivity<RetrofitDemoActivity>()
        }

        fragment_factory_button.setOnClickListener {
            /** lunch with FragmentFactoryDemoActivity**/
            startActivity<FragmentFactoryDemoActivity>()
        }
    }


}