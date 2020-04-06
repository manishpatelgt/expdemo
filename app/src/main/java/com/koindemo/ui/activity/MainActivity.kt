package com.koindemo.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.koindemo.R
import com.koindemo.data.PreferencesHelper
import com.koindemo.databinding.ActivityMainBinding
import com.koindemo.model.Post
import com.koindemo.services.MyService
import com.koindemo.ui.adapter.PostListAdapter
import com.koindemo.ui.fragments.MainFragment
import com.koindemo.utils.network.NetworkHelper
import com.koindemo.utils.extensions.getCustomColor
import com.koindemo.utils.extensions.hide
import com.koindemo.utils.extensions.isAtLeastAndroid8
import com.koindemo.utils.extensions.show
import com.koindemo.utils.hardware.HardwareHelper
import com.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Created by Manish Patel on 3/31/2020.
 */
/**
 * https://proandroiddev.com/koin-in-feature-modules-project-6329f069f943
 * https://medium.com/@harmittaa/setting-up-koin-2-0-1-for-android-ebf11de01816
 * https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-network-error-handling-a5b98b5e5ca0
 */
class MainActivity : AppCompatActivity(), PostListAdapter.OnItemClickListener {

    private lateinit var mViewBinding: ActivityMainBinding

    private lateinit var mAdapter: PostListAdapter

    // lazy inject MyViewModel
    val mViewModel: MainViewModel by viewModel()

    //private val mainFragment: MainFragment by inject() // Property Injection

    private val mainFragment: MainFragment = get() // Direct Instance Request

    private val preferencesHelper: PreferencesHelper by inject() // Property Injection

    private val context: Context by inject() // Property Injection

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mViewBinding.root
        setContentView(view)

        mAdapter = PostListAdapter(this)

        // Initialize RecyclerView
        mViewBinding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        /** set observer **/
        setObservers()

        if (savedInstanceState == null) {

            /** Load fragment **/
            supportFragmentManager.beginTransaction()
                .add(mViewBinding.fragmentContainerView.id, mainFragment)
                .commit()
        }

        /** start service here **/
        lifecycleScope.launch(Dispatchers.IO) {
            delay(10000) //10 sec delay
            startService()
        }

        /** set/get Handheld ID **/
        preferencesHelper.handheldID = HardwareHelper.getDeviceUUID(context)
        println("HHID: ${preferencesHelper.handheldID}")
    }

    fun startService() {
        if (isAtLeastAndroid8()) {
            startForegroundService(Intent(applicationContext, MyService::class.java))
        } else {
            startService(Intent(applicationContext, MyService::class.java))
        }
    }

    private fun setObservers() {

        //mViewModel.getPosts()

        /** Setup posts api observer  */
        mViewModel.postList?.observe(this, Observer { entries ->
            Timber.d("Posts: $entries")
            mAdapter.setPosts(entries)
        })


        mViewModel.getPostsWithLiveData.observe(this, Observer { entries ->
            Timber.d("Got Posts data: $entries")
            mAdapter.setPosts(entries)
        })

        /**
         * Observe network changes i.e. Internet Connectivity
         */
        NetworkHelper.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            println("STATE CHANGED = $isConnected")
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getCustomColor(R.color.colorStatusNotConnected))
                }
            } else {

                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(getCustomColor(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    override fun onItemClicked(post: Post) {
        println("Clicked: ${post.id}")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> true
        }
    }

    override fun onBackPressed() {
        MaterialDialog.Builder(this)
            .setTitle("Exit?")
            .setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(applicationContext, MyService::class.java))
        Timber.e("Main Activity onDestroy()")
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }
}