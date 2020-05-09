package com.expdemo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.expdemo.R
import com.expdemo.databinding.ActivityMainBinding
import com.expdemo.ui.jetpackpaging.PagingActivity
import com.expdemo.ui.retrofitdemo.RetrofitDemoActivity
import com.expdemo.ui.coroutinetest.CoroutineTestActivity
import com.expdemo.ui.coroutinetest.CoroutineTestActivity2
import com.expdemo.ui.fragmentdatasharing.FragmentSharingDataActivity
import com.expdemo.ui.fragmentfactorydemo.FragmentFactoryDemoActivity
import com.expdemo.ui.settingspanel.SettingsPanelActivity
import com.expdemo.ui.viewmodelstatedemo.ViewModelStateDemoActivity
import com.expdemo.ui.udacitydemo.MarsPhotoListDemo
import com.expdemo.ui.vb.ViewBindingDemoActivity
import com.expdemo.ui.withoutsingelton.WithoutSingletonActivity
import com.expdemo.ui.withsingelton.WithSingletonActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = lifecycleOwner
            viewModel = mViewModel
        }

        /** Set action bar */
        setSupportActionBar(binding.toolbar)

        mViewModel.screen.observe(this, Observer {
            when (it) {
                MainViewModel.Screen.WITHOUTSINGLETONDEMO -> {
                    /** lunch without singleton activity**/
                    startActivity<WithoutSingletonActivity>()
                }
                MainViewModel.Screen.WITHSINGLETONDEMO -> {
                    /** lunch with singleton activity**/
                    startActivity<WithSingletonActivity>()
                }
                MainViewModel.Screen.COROUTINE1DEMO -> {
                    /** lunch with CoroutineTestActivity**/
                    startActivity<CoroutineTestActivity>()
                }
                MainViewModel.Screen.COROUTINE2DEMO -> {
                    /** lunch with CoroutineTestActivity2**/
                    startActivity<CoroutineTestActivity2>()
                }
                MainViewModel.Screen.RETROFITDEMO -> {
                    /** lunch with RetrofitDemoActivity**/
                    startActivity<RetrofitDemoActivity>()
                }
                MainViewModel.Screen.FRAGMENTFACTORYDEMO -> {
                    /** lunch with FragmentFactoryDemoActivity**/
                    startActivity<FragmentFactoryDemoActivity>()
                }
                MainViewModel.Screen.VIEWMODELSTATEDEMO -> {
                    /** lunch with ViewModelStateDemoActivity**/
                    startActivity<ViewModelStateDemoActivity>()
                }
                MainViewModel.Screen.JETPACKPAGINGDEMO -> {
                    /** lunch with PagingActivity**/
                    startActivity<PagingActivity>()
                }
                MainViewModel.Screen.VIEWBIDINGDEMO -> {
                    /** lunch with ViewBindingDemoActivity**/
                    startActivity<ViewBindingDemoActivity>()
                }
                MainViewModel.Screen.MARSLISTDEMO -> {
                    /** lunch with MarsPhotoListDemo**/
                    startActivity<MarsPhotoListDemo>()
                }
                MainViewModel.Screen.FRAGMENTSHARINGDATADEMO -> {
                    /** lunch with FragmentSharingDataActivity**/
                    startActivity<FragmentSharingDataActivity>()
                }
                MainViewModel.Screen.SETTINGSPANEL -> {
                    /** lunch with SettingsPanelActivity**/
                    startActivity<SettingsPanelActivity>()
                }
            }
        })
    }

}