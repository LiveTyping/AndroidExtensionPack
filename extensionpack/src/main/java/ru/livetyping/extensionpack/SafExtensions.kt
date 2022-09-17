package ru.livetyping.extensionpack

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.exifinterface.media.ExifInterface

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(ACCESS_MEDIA_LOCATION)
fun Context.flipImageHorizontally(uri: Uri) {
    val photoUri = MediaStore.setRequireOriginal(uri)
    contentResolver.openInputStream(photoUri).use { stream ->
        stream?.let {
            ExifInterface(it).run {
                flipHorizontally()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(ACCESS_MEDIA_LOCATION)
fun Context.flipImageVertically(uri: Uri) {
    val photoUri = MediaStore.setRequireOriginal(uri)
    contentResolver.openInputStream(photoUri).use { stream ->
        stream?.let {
            ExifInterface(it).run {
                flipVertically()
            }
        }
    }
}