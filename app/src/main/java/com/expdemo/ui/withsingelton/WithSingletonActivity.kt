package com.expdemo.ui.withsingelton

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.expdemo.R
import com.expdemo.application.App
import com.expdemo.models.Post
import com.expdemo.viewmodel.MainViewModel
import com.expdemo.viewmodel.MainViewModelFactory
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_common.*
import timber.log.Timber

/** https://developer.android.com/training/dependency-injection/manual#dependencies-container **/
class WithSingletonActivity : AppCompatActivity() {

    /** ViewModel */
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)

        /** Set action bar */
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Gets userRepository from the instance of AppContainer in Application
        val appContainer = (application as App).appContainer
        val userRepository = appContainer.userRepository

        mainViewModel =
            ViewModelProviders.of(this, MainViewModelFactory(userRepository))
                .get(MainViewModel::class.java)

        /** Set observers*/
        setObservers()
    }

    private fun setObservers() {

        if (mainViewModel.network) {
            //mainViewModel.getPosts()
            //mainViewModel.getPostsWithAwait()

            mainViewModel.getPostsWithLiveData.observe(this, Observer { entries ->
                Timber.d("Got Posts data: $entries")
                txt_view.text = entries.toString()
            })

        } else {
            mainViewModel.setToastMessage(getString(R.string.no_internet_text))
        }

        /** Setup categories api observer  */
        mainViewModel.postList?.observe(this, Observer { entries ->
            Timber.d("Posts: $entries")
            txt_view.text = entries.toString()
        })

        /** Set observer for a toast message */
        val observerShowToast = Observer<String> {
            showToastMessage(it)
        }

        mainViewModel._showToast.observe(this, observerShowToast)

        /** Setup api error observer  */
        mainViewModel.apiError.observe(this, Observer<String> {
            it?.let {
                mainViewModel.setToastMessage(it)
            }
        })

        /** get list of post **/
        mainViewModel.getPostFromDB.observe(this, Observer { entries ->
            Timber.d("Posts: $entries")
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /** Show toast message */
    private fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }
}