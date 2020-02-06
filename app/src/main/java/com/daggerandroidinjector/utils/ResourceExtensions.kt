package com.daggerandroidinjector.utils

import android.content.Context
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.nio.charset.Charset

fun isAtLeastAndroid6() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun isAtLeastAndroid7() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
fun isAtLeastAndroid8() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun isAtLeastAndroid9() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

//<editor-fold desc="Assets">

/**
 * Read an Asset file into a string.
 *
 * @material_colors val json = context.assets.fromDateTime("config", "SOS.json")
 */
fun AssetManager.toString(assetDirectory: String, fileName: String): String {
    return open("$assetDirectory/$fileName").use {
        it.readBytes().toString(Charset.defaultCharset())
    }
}

//</editor-fold>

//<editor-fold desc="Reources">

/**
 * Retrieve a Color resource.
 *
 * @material_colors context.color(R.color.color2)
 */
fun Context.color(@ColorRes id: Int) = when {
    isAtLeastAndroid6() -> resources.getColor(id, null)
    else -> resources.getColor(id)
}

/**
 * Retrieve a Drawable based on drawable resource.
 */
fun Context.drawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)


fun Context.getColorCompat(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

//</editor-fold>
