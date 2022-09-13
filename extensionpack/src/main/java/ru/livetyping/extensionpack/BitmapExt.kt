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

fun View.getBitmapFromResourceDrawable(@DrawableRes res: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context, res)
    return getBitmapFromDrawable(drawable ?: return null)
}

fun Int.getBitmapResource(resources: Resources, useScale: Boolean = false) =
    BitmapFactory.decodeResource(resources, this, BitmapFactory.Options().apply {
        inScaled = useScale
    })

fun String.getBitmapFromAsset(context: Context): Bitmap? {
    val assetManager: AssetManager = context.assets
    val stream: InputStream
    var bitmap: Bitmap? = null
    try {
        stream = assetManager.open(this)
        bitmap = BitmapFactory.decodeStream(stream)
    } catch (e: IOException) {
    }
    return bitmap
}

fun String.getBitmapFromUrl(): Bitmap {
    val url = URL(this)
    return BitmapFactory.decodeStream(url.openConnection().getInputStream())
}

fun Uri.toBitmap(context: Context): Bitmap? {
    val imageStream: InputStream = context
        .contentResolver
        .openInputStream(this) ?: return null
    return BitmapFactory.decodeStream(imageStream)
}

fun View.getBitmapFromDrawable(mDrawable: Drawable): Bitmap? {
    val drawable = (DrawableCompat.wrap(mDrawable)).mutate()
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Bitmap.getLocalBitmapUri(context: Context): Uri? {
    var bmpUri: Uri? = null
    try {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        val out = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 50, out)
        out.close()
        bmpUri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}

fun ImageView.getBitmap(): Bitmap? {
    val bitmap: Bitmap
    if (drawable is BitmapDrawable) {
        bitmap = (drawable as BitmapDrawable).bitmap
    } else {
        drawable ?: return null
        bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
    }
    return bitmap
}


fun Bitmap.createLocalBitmapAndGetUri(context: Context): Uri? {
    var bmpUri: Uri? = null
    try {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png"
        )
        val out = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}

fun File.compressImage(context: Context) = BitmapFactory.decodeFile(path).createTempBitmapFile(context)

fun Bitmap.createTempBitmapFile(context: Context): File? {
    var file: File? = null
    try {
        file = File.createTempFile(
            "tempFile-${this.hashCode()}-${SystemClock.elapsedRealtime()}",
            ".png",
            context.cacheDir
        )
        val out = FileOutputStream(file)
        out.write(compressToSize())
        out.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}


fun ByteArray.createTempByteArrayFile(context: Context): File? {
    val file: File?
    try {
        file = File.createTempFile("tempFile", ".png", context.cacheDir)
        val out = FileOutputStream(file)
        out.write(this)
        out.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return file
}

fun Bitmap.compressToSize(size: Int = 2_097_152): ByteArray {
    var byteArray = compressBitmapToByteArray()
    if (byteArray.size < size) {
        return byteArray
    }

    var newBitmap = this.copy(Bitmap.Config.ARGB_8888, true)
    this.recycle()
    byteArray = newBitmap.compressBitmapToByteArray()
    newBitmap.recycle()
    if (byteArray.size < size) {
        return byteArray
    }

    for (i in 0 until 10) {
        newBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val resizedBitmap = newBitmap.resizeBitmapForRatio(0.9f)
        newBitmap.recycle()

        byteArray = resizedBitmap.compressBitmapToByteArray()
        resizedBitmap.recycle()

        if (byteArray.size < size) {
            return byteArray
        }
    }

    return byteArray
}

fun Bitmap.resizeBitmapForRatio(decreaseRatio: Float): Bitmap {
    if (decreaseRatio <= 0 || decreaseRatio >= 1) {
        return this
    }
    val maxLength = if (this.height >= this.width) {
        this.height * decreaseRatio
    } else {
        this.width * decreaseRatio
    }
    return this.resizeBitmap(maxLength.toInt())
}

fun Bitmap.resizeBitmap(maxHeight: Int): Bitmap {
    try {
        if (this.height >= this.width) {
            if (this.height <= maxHeight) { // if image height already smaller than the required height
                return this
            }

            val aspectRatio = this.width.toDouble() / this.height.toDouble()
            val targetWidth = (maxHeight * aspectRatio).toInt()
            return Bitmap.createScaledBitmap(this, targetWidth, maxHeight, false)
        } else {
            if (this.width <= maxHeight) { // if image width already smaller than the required width
                return this
            }

            val aspectRatio = this.height.toDouble() / this.width.toDouble()
            val targetHeight = (maxHeight * aspectRatio).toInt()

            return Bitmap.createScaledBitmap(this, maxHeight, targetHeight, false)
        }
    } catch (e: Exception) {
        return this
    }
}

fun Bitmap.compressBitmapToByteArray(quality: Int = 90): ByteArray {
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, bos)
    return bos.toByteArray()
}

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
