package ru.livetyping.extensionpack

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import java.io.*
import java.net.URL

@RequiresApi(Build.VERSION_CODES.N)
fun Bitmap.checkAndRotateBitmap(context: Context, path: Uri): Bitmap {
    return try {
        val orientation: Int = getImageOrientation(context, path)
        if (orientation > 0) rotate(this, orientation) else this
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

private fun rotate(bitmap: Bitmap, rotate: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(rotate.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

@RequiresApi(Build.VERSION_CODES.N)
private fun getImageOrientation(context: Context, path: Uri): Int {
    var rotate = 0
    val stream = context.contentResolver.openInputStream(path)
    try {
        stream?.let {
            val exif = ExifInterface(it)
            rotate = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) {
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                else -> 0
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        stream?.close()
    }
    return rotate
}
