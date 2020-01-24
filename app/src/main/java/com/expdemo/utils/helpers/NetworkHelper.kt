package com.expdemo.utils.helpers

import android.content.Context
import android.net.NetworkInfo
import android.net.ConnectivityManager

object NetworkHelper {

    fun connectedToNetwork(context: Context): Boolean {
        val networkInfo = getNetworkInfo(context)
        return networkInfo != null && networkInfo!!.isConnectedOrConnecting
    }

    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }
}