package com.blepoc.activities

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blepoc.R
import com.blepoc.utility.Utils
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest

/**
 * Created by Manish Patel on 8/4/2020.
 */
open class BaseActivity : AppCompatActivity() {

    fun rationaleCallback(req: QuickPermissionsRequest, permission: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_title2))
            .setMessage(Utils.getRationaleMessage(permission))
            .setCancelable(true)
            .setPositiveButton(android.R.string.yes) { dialog, _ -> req.proceed() }
            .create()
            .show()
    }

    fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(getString(R.string.permission_title2))
        alertBuilder.setMessage(getString(R.string.permission_message2))
        alertBuilder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            req.openAppSettings()
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}