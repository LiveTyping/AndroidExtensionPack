package ru.livetyping.extensionpack

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.exifinterface.media.ExifInterface
import java.io.*

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(ACCESS_MEDIA_LOCATION)
inline fun Context.getLocationFromImages(uri: Uri, latNLongCallBack: (latNLong: DoubleArray) -> Unit = { _ -> }) {
    val photoUri = MediaStore.setRequireOriginal(uri)
    contentResolver.openInputStream(photoUri).use { stream ->
        stream?.let {
            ExifInterface(it).run {
                val latLong = this.latLong ?: doubleArrayOf(0.0, 0.0)
                latNLongCallBack(latLong)
            }
        }
    }
}

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


inline fun Context.moveFileToUri(treeUri: Uri, file: File, crossinline progress: (Long) -> Unit = {}) {
    contentResolver.openOutputStream(treeUri)?.use { output ->
        output as FileOutputStream
        FileInputStream(file).use { input ->
            output.channel.truncate(0)
            copyStream(file.length(), input, output) {
                progress.invoke(it)
            }
        }
    }
}

fun getMimeType(filePath: String?): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun File.getMimeType(): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun File.getMimeType(fallback: String = "*/*"): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase()) }
        ?: fallback
}

inline fun copyStream(
    size: Long,
    inputStream: InputStream,
    os: OutputStream,
    bufferSize: Int = 4096,
    progress: (Long) -> Unit = {}
) {
    try {
        val bytes = ByteArray(bufferSize)
        var count = 0
        var prog = 0
        while (count != -1) {
            count = inputStream.read(bytes)
            if (count != -1) {
                os.write(bytes, 0, count)
                prog += count
                progress(prog.toLong() * 100 / size)
            }
        }
        os.flush()
        inputStream.close()
        os.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

inline fun InputStream.copyStream(size: Long, os: OutputStream, bufferSize: Int = 4096, progress: (Long) -> Unit = {}) {
    try {
        val bytes = ByteArray(bufferSize)
        var count = 0
        var prog = 0
        while (count != -1) {
            count = read(bytes)
            if (count != -1) {
                os.write(bytes, 0, count)
                prog += count
                progress(prog.toLong() * 100 / size)
            }
        }
        os.flush()
        close()
        os.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

