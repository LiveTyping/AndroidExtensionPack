package ru.livetyping.extensionpack

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Field
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(pattern: String): Date? =
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)

fun String.getImageByString(context: Context) =
    ContextCompat.getDrawable(
        context,
        context.resources.getIdentifier(this, "drawable", context.packageName)
    )

fun String.getId(c: Class<*>): Int {
    try {
        val idField: Field = c.getDeclaredField(this)
        return idField.getInt(idField)
    } catch (e: Exception) {
        throw RuntimeException("No resource ID found for: $this / $c", e)
    }
}

fun String.parseCommaFloat() = this.replace(",", ".").toFloat()

fun String.toHtmlString() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()

fun String.downloadVideo(context: Context): File? {
    val u = URL(this)
    val conn: URLConnection = u.openConnection()
    val contentLength: Int = conn.contentLength
    val stream = DataInputStream(u.openStream())
    val buffer = ByteArray(contentLength)
    stream.readFully(buffer)
    stream.close()
    val file = File.createTempFile("tempFile", ".mp4", context.cacheDir)
    val fos = DataOutputStream(FileOutputStream(file))
    fos.write(buffer)
    fos.flush()
    fos.close()
    return file
}

fun String.colorText(@ColorInt color: Int) =
    SpannableString(this).apply {
        setSpan(
            ForegroundColorSpan(color),
            0,
            this@colorText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

fun String.colorText(startIndex: Int = 0, endIndex: Int, @ColorInt color: Int) =
    SpannableString(this).apply {
        setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }