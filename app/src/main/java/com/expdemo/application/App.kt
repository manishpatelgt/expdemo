package com.expdemo.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class App : MultiDexApplication() {

    /** Coroutine scope to delayed initialization*/
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // Instance of AppContainer that will be used by all the Activities of the app
    //val appContainer = AppContainer(this)

    lateinit var appContainer: AppContainer

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // initialise app as a singleton
        sInstance = this

        appContainer = AppContainer()

        /**
         * THREETENABP
         * */
        AndroidThreeTen.init(this)

        /**
         * STETHO
         * */
        Stetho.initializeWithDefaults(this)

        /**
         * TIMBER
         * */

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        // Needs to be volatile as another thread can see a half initialised instance.
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var sInstance: App

        fun getInstance(): App {
            if (sInstance == null) {
                synchronized(App::class.java) {
                    if (sInstance == null) {
                        sInstance = App()
                    }
                }
            }
            return sInstance
        }

        val context: Context
            get() = sInstance.applicationContext

    }
}