package com.daggerandroidinjector.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.daggerandroidinjector.R
import com.daggerandroidinjector.repository.PostRepository
import com.daggerandroidinjector.utils.NotificationHelper
import com.daggerandroidinjector.utils.isAtLeastAndroid8
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Manish Patel on 2/6/2020.
 */
class MyService : Service() {

    @Inject
    lateinit var postRepository: PostRepository

    private val rootJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + rootJob)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        Timber.d("MyService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("MyService onStartCommand")

        if (isAtLeastAndroid8()) {
            val notificationHelper = NotificationHelper(this)
            val foregroundNotification =
                notificationHelper.getForegroundServiceNotification(
                    "Service Running",
                    "GPS Location updated.",
                    null
                )
            startForeground(NotificationHelper.SERVICE_RUNNING_NOTIFICATION, foregroundNotification)
        }

        ioScope.launch {
            delay(6000) // 6 sec delay
            callAPI()
        }
        return Service.START_STICKY
    }

    fun callAPI() {
        ioScope.launch {
            postRepository.callPostApi2()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rootJob.cancel()
        Timber.d("FusedLocationService onDestroy")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Timber.d("FusedLocationService onTaskRemoved")
        super.onTaskRemoved(rootIntent)
    }
}