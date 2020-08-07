package com.blepoc.utility

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.blepoc.App.Companion.context
import com.blepoc.BuildConfig
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Manish Patel on 8/4/2020.
 */
object Utils {

    /** local sqlite database wise fields **/
    val APPLICATION_PACKAGE = BuildConfig.APPLICATION_ID
    val APPLICATION_PATH = "/data/data/$APPLICATION_PACKAGE"
    val APPLICATION_DATABASE_NAME = "ble.db"

    val NOTIFICATION_TITLE = "Service Running"
    val NOTIFICATION_MESSAGE = "Executing background operations"

    const val BLE_MESSAGE = "Please turn ON Bluetooth"

    var SCAN_CHECK_INTERVAL_TIME = 20 * 1000.toLong()
    var ALERT_MINUTES = 5

    fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun serviceIsRunning(context: Context, serviceName: String): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName == service.service.className) {
                return true
            }
        }
        return false
    }


    fun getRationaleMessage(permission: String): String {
        return when (permission) {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION -> "Location - This app needs permission to access your current location in order to scan bluetooth"

            else -> return "Rationale Message NOT Found!"
        }
    }

    val calendar: Calendar = Calendar.getInstance()

    fun getCurrentTimeStamp(): Long {
        val stamp = Timestamp(System.currentTimeMillis())
        return stamp.time
    }

    fun getDifferenceInMinutes(startTime: Long, endTime: Long): Long {
        val timeDiff = startTime - endTime
        return TimeUnit.MILLISECONDS.toMinutes(timeDiff)
    }

    fun getCurrentDateTimeString(): String {
        return simpleDateFormat.format(calendar.timeInMillis)
    }

    val TIMESTAMP_LONG_DATE_TIME_24_HOUR = "dd/MM/yyyy HH:mm:ss"
    var simpleDateFormat = SimpleDateFormat(TIMESTAMP_LONG_DATE_TIME_24_HOUR)

}