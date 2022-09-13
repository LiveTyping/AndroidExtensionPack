package ru.livetyping.extensionpack.strings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import org.intellij.lang.annotations.RegExp
import ru.livetyping.extensionpack.tryOrElse
import java.io.File
import java.net.URLEncoder
import java.text.DateFormat
import java.text.ParseException
import java.util.*

fun String.removeSymbols(replacement: String = "�"): String {
    return this.replace(Regex("[^\\p{ASCII}‘’]"), replacement)
}

fun String.containsAny(vararg strings: String): Boolean {
    return strings.any { this.contains(it) }
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}

fun String.camelCaseWords(): String {
    return this.lowercase().capitalizeWords()
}

fun String.trimTo(length: Int): String {
    return if (this.length < length) {
        this
    } else {
        this.substring(0, length - 1).plus("…")
    }
}

fun List<String>.containsCaseInsensitive(string: String): Boolean {
    forEach {
        if (it.equals(string, true)) {
            return true
        }
    }
    return false
}

fun List<String>.indexCaseInsensitive(string: String): Int {
    forEachIndexed { index, s ->
        if (s.equals(string, true)) {
            return index
        }
    }
    return -1
}

val arabicChars = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

fun String.asArabicNumbers(): String {
    if (TextUtils.isEmpty(this))
        return ""

    val builder = StringBuilder()
    for (i in this.indices) {
        if (Character.isDigit(this[i])) {
            builder.append(arabicChars[this[i].toInt() - 48])
        } else {
            builder.append(this[i])
        }
    }
    return "" + builder.toString()
}

fun String.parseAssetFile(): Uri = Uri.parse("file:///android_asset/$this")

fun String.parseInternalStorageFile(absolutePath: String): Uri = Uri.parse("$absolutePath/$this")


fun String.parseFile(): Uri = Uri.fromFile(File(this))

val Uri.fileExists: Boolean
    get() = File(toString()).exists()

fun String.remove(value: String, ignoreCase: Boolean = false): String = replace(value, "", ignoreCase)

fun String.removeNumberFormat(): String = remove(",")

fun String.removeNumberFormatDot(): String = remove(".")

val NON_DIGIT_REGEX = Regex("[^A-Za-z0-9]")
val DIGIT_REGEX = Regex("[^0-9]")

fun String?.replaceNonDigit(replacement: String = "") = this?.replace(NON_DIGIT_REGEX, replacement)

fun String?.replaceDigit(replacement: String = "") = this?.replace(DIGIT_REGEX, replacement)

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.removeSpace(): String = replace(" ", "")

fun String.toDate(format: DateFormat): Date? {
    return try {
        format.parse(this)
    } catch (exc: ParseException) {
        exc.printStackTrace()
        null
    }
}

fun String.getRequestUrl(vararg fields: Pair<String, Any>): String {

    val param = StringBuffer()
    for (item in fields) {
        param.append(item.first + "=" + URLEncoder.encode(item.second.toString(), "UTF-8") + "&")
    }
    val paramStr = param.toString().let {
        it.substring(0, it.length - 1)
    }
    return if (indexOf("?") >= 0) {
        "$this&$paramStr"
    } else {
        "$this?$paramStr"
    }
}

fun String.remove(@RegExp pattern: String) = remove(Regex(pattern, RegexOption.IGNORE_CASE))

fun String.remove(regex: Regex) = replace(regex, "")

fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}

fun String?.openInBrowser(context: Context?) {
    if (!this.isNullOrEmpty()) {
        val page = Uri.parse(this)
        val intent = Intent(Intent.ACTION_VIEW, page)

        context?.apply {
            tryOrElse {
                context.startActivity(intent)
            }
        }

    }
}

fun String.removeSpaces(): String {
    return this.replace(" ", "")
}

fun String.versionNumberToInt(): Int {
    return split(".").joinToString("").toInt()
}

fun String.capitalizeFirstLetterEachWord(): String {
    return this.lowercase()
        .split(" ")
        .joinToString(" ") { it.capitalize() }
}

fun String.toColor(): Int? {
    return if (this.isNotEmpty()) {
        Color.parseColor(this)
    } else {
        null
    }
}