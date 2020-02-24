package com.expdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.R
import com.expdemo.ui.retrofitdemo.RetrofitDemoActivity
import com.expdemo.ui.test.CoroutineTest.CoroutineTestActivity
import com.expdemo.ui.test.CoroutineTest.CoroutineTestActivity2
import com.expdemo.ui.test.fragmentfactorydemo.FragmentFactoryDemoActivity
import com.expdemo.ui.test.viewmodelstatedemo.ViewModelStateDemoActivity
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

        coroutine_test_2_button.setOnClickListener {
            /** lunch with CoroutineTestActivity2**/
            startActivity<CoroutineTestActivity2>()
        }

        retrofit_generic_button.setOnClickListener {
            /** lunch with RetrofitDemoActivity**/
            startActivity<RetrofitDemoActivity>()
        }

        fragment_factory_button.setOnClickListener {
            /** lunch with FragmentFactoryDemoActivity**/
            startActivity<FragmentFactoryDemoActivity>()
        }

        view_model_state_button.setOnClickListener {
            /** lunch with ViewModelStateDemoActivity**/
            startActivity<ViewModelStateDemoActivity>()
        }
    }


}