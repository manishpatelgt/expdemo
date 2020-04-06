package com.koindemo.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.koindemo.utils.hardware.HardwareHelper

/**
 * Created by Manish Patel on 4/6/2020.
 */
class PreferencesHelper(val context: Context) {

    private val PREF_KEY_HANDHELD_ID = "HandheldID"  // IMEI or DeviceID
    var preferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE)

    private fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    var handheldID: String
        get() {
            return getString(PREF_KEY_HANDHELD_ID) ?: HardwareHelper.getDeviceUUID(context)
        }
        set(value) {
            putString(PREF_KEY_HANDHELD_ID, value)
        }

    private fun getString(key: String, defValue: String? = ""): String? {
        return if (preferences.getString(key, defValue).equals("null")) {
            defValue
        } else {
            preferences.getString(key, defValue)
        }
    }

    private fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    private fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }
}