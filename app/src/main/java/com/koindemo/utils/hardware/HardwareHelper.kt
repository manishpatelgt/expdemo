package com.koindemo.utils.hardware

import android.content.Context
import android.text.TextUtils
import android.provider.Settings;

object HardwareHelper {

    /**
     * Gets the device unique ID number (either IMEI or Android_ID). If both numbers are not found,
     * reads UUID number.
     */
    fun getDeviceUUID(context: Context): String {

        var identifier: String = getAndroidId(context)
        if (TextUtils.isEmpty(identifier)) {
            identifier = Installation.id(context)
        }
        return identifier
    }

    /**
     * Android_ID is a 64-bit hex number that is generated and stored when the device first boots.
     * It is reset when the device is wiped. It should remain constant for the whole lifetime of user's device.
     * Android 4.2.2 and newer versions support multiple user accounts, each one having a unique Android ID.
     */
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}