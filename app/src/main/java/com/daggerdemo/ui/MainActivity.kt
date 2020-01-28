package com.daggerdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daggerdemo.R
import com.daggerdemo.application.App
import com.daggerdemo.viewmodels.MainViewModel
import com.daggerdemo.viewmodels.MainViewModelFactory
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    /** ViewModel */
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** singleton **/
        val userRepository = App.getInstance().appComponent.repository()

        mainViewModel =
            ViewModelProviders.of(this, MainViewModelFactory(userRepository))
                .get(MainViewModel::class.java)

        /** Set observers*/
        setObservers()
    }

    private fun setObservers() {

        if (mainViewModel.network) {
            mainViewModel.getPosts()
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
    }

    /** Show toast message */
    private fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }
}