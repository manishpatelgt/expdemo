package com.blepoc.utility.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.blepoc.R
import com.blepoc.activities.MainActivity
import com.blepoc.utility.isAtLeastAndroid8
import com.blepoc.utility.notificationManager

class NotificationHelper(val context: Context) : ContextWrapper(context) {

    private var manager: NotificationManager =
        context.notificationManager

    init {
        // Notifications channels must be registered on Android 8.0 and higher, otherwise notifications will never show up to the user.
        createForegroundServicesChannel()
    }

    private fun createForegroundServicesChannel() {
        if (isAtLeastAndroid8() && manager.getNotificationChannel(SERVICE_RUNNING_CHANNEL_ID) == null) {
            val foregroundServices =
                NotificationChannel(
                    SERVICE_RUNNING_CHANNEL_ID,
                    SERVICE_RUNNING_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            foregroundServices.description = SERVICE_RUNNING_CHANNEL_DESCRIPTION
            foregroundServices.lightColor = Color.BLUE
            foregroundServices.setShowBadge(false)
            foregroundServices.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(foregroundServices)
        }
    }

    fun updateNotification(
        title: String,
        message: String
    ) {
        val updatedNotification = getForegroundServiceNotification(title, message)
        manager.notify(
            SERVICE_RUNNING_NOTIFICATION,
            updatedNotification
        )
    }

    fun getForegroundServiceNotification(
        title: String,
        message: String
    ): Notification {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        //intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, SERVICE_RUNNING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)   // android.R.drawable.stat_notify_chat
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
        private const val SERVICE_RUNNING_CHANNEL_NAME = "Frisbee Foreground Services Running"
        private const val SERVICE_RUNNING_CHANNEL_DESCRIPTION =
            "Frisbee Running as a Foreground Service"

        const val SERVICE_RUNNING_NOTIFICATION = 1202
    }
}