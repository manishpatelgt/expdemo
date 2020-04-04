package com.koindemo.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.koindemo.repository.PostRepository
import com.koindemo.utils.extensions.NotificationHelper
import com.koindemo.utils.extensions.isAtLeastAndroid8
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Created by Manish Patel on 4/4/2020.
 */
class MyService : Service() {

    private val rootJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + rootJob)

    private val postRepository: PostRepository by inject()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
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