package com.daggerandroidinjector.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daggerandroidinjector.R
import com.daggerandroidinjector.utils.viewModelProvider
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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

        /** Set observers*/
        setObservers()
    }

    private fun setObservers() {

        if (model.network) {
            model.getPosts()
        } else {
            model.setToastMessage(getString(R.string.no_internet_text))
        }

        /** Setup categories api observer  */
        model.posts.observe(this, Observer { posts ->
            Timber.d("Posts: $posts")
            txt_view.text = posts.toString()
        })

        /** Set observer for a toast message */
        val observerShowToast = Observer<String> {
            showToastMessage(it)
        }

        model._showToast.observe(this, observerShowToast)

        /** Setup errorMessage observer  */
        model.errorMessage.observe(this, Observer {
            model.setToastMessage(it)
        })

        model.showProgress.observe(this, Observer { showProgress ->
            Timber.d("showProgress: $showProgress")
            if (showProgress) {
                txt_view.text = "Loading..."
            }
        })
    }

    /** Show toast message */
    private fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }
}