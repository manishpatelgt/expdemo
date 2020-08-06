package com.blepoc.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blepoc.utility.notifications.NotificationHelper

/**
 * Created by Manish Patel on 8/6/2020.
 */
class NotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            /** Clear notification **/
            NotificationHelper(context).clearNotification()
        }
    }
}