package com.daggerandroidinjector.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.daggerandroidinjector.R
import com.kotlindemo.utility.notificationManager

class NotificationHelper(val context: Context) : ContextWrapper(context) {
    private var manager: NotificationManager =
        context.notificationManager //context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        // Notifications channels must be registered on Android 8.0 and higher, otherwise notifications will never show up to the user.
        createForegroundServicesChannel()
    }

    //<editor-fold desc="Notification Channels">

    private fun createForegroundServicesChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && manager.getNotificationChannel(
                SERVICE_RUNNING_CHANNEL_ID
            ) == null) {
            val foregroundServices =
                NotificationChannel(
                    SERVICE_RUNNING_CHANNEL_ID,
                    SERVICE_RUNNING_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            foregroundServices.description = SERVICE_RUNNING_CHANNEL_DESCRIPTION
            foregroundServices.lightColor = Color.BLUE
            foregroundServices.setShowBadge(false)
            foregroundServices.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(foregroundServices)
        }
    }


    //</editor-fold>
    fun getForegroundServiceNotification(title: String, message: String, pendingIntent: PendingIntent?): Notification {
        val builder = NotificationCompat.Builder(this, SERVICE_RUNNING_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)   // android.R.drawable.stat_notify_chat
            .setContentTitle(title)
            .setContentText(message)
            .setColorized(true)

            // do not allow the system to use the default notification sound
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // for Android 7.1 and lower
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

        // This is what to do when the user taps notification
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        }

        return builder.build()
    }


    companion object {
       private const val SERVICE_RUNNING_CHANNEL_ID = "ForegroundServicesChannel"
        private const val SERVICE_RUNNING_CHANNEL_NAME = "Foreground Services Running"
        private const val SERVICE_RUNNING_CHANNEL_DESCRIPTION = "Running as a Foreground Service"

        const val SERVICE_RUNNING_NOTIFICATION = 1202
    }
}
