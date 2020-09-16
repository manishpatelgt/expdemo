package com.blepoc.utility.hardware;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class HardwareHelper {

    /**
     * Gets the device unique ID number (either IMEI or Android_ID). If both numbers are not found,
     * reads UUID number.
     */
    public static String getDeviceUUID(Context context) {

        String identifier = getAndroidId(context);

        if (TextUtils.isEmpty(identifier)) {
            identifier = Installation.id(context);
        }

        return identifier;
    }

    /**
     * Android_ID is a 64-bit hex number that is generated and stored when the device first boots.
     * It is reset when the device is wiped. It should remain constant for the whole lifetime of user's device.
     * Android 4.2.2 and newer versions support multiple user accounts, each one having a unique Android ID.
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
