package com.expdemo.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Debug
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

        val dateFormat = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
        val logName = "gofast-${dateFormat.format(Date())}.trace"
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("files", Context.MODE_PRIVATE)
        var outputFile = File(file, logName)

        /** start the trace **/
        //Debug.startMethodTracingSampling(outputFile.name, 1030, 100)

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