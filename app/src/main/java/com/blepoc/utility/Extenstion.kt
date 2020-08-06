package com.blepoc.utility

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import com.blepoc.App.Companion.context

/**
 * Created by Manish Patel on 4/28/2020.
 */

fun isAtLeastAndroid5() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
fun isAtLeastAndroid6() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun isAtLeastAndroid7() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
fun isAtLeastAndroid8() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun isAtLeastAndroid9() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun isNetworkAvailable(): Boolean = NetworkHelper.connectedToWiFiOrMobile(context)

inline val Context.locationManager: LocationManager?
    get() = getSystemService(Context.LOCATION_SERVICE) as? LocationManager

inline val Context.alarmManager: AlarmManager?
    get() = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

inline val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

inline val Context.devicePolicyManager: DevicePolicyManager?
    get() = getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager

inline val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager

inline val Context.inputManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

inline val Context.sensorManager: SensorManager
    get() = getSystemService(Context.SENSOR_SERVICE) as SensorManager

inline val Context.bluetoothManager: BluetoothManager?
    get() = getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
