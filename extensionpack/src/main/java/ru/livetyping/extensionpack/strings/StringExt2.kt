package ru.livetyping.extensionpack.strings

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.regex.Pattern

inline fun String.ifIsNullOrEmpty(action: () -> Unit) {
    if (isNullOrEmpty()) action()
}

inline fun String.ifIsNotNullOrEmpty(action: () -> Unit) {
    if (!isNullOrEmpty()) action()
}

fun String.urlEncoded(): String? = URLEncoder.encode(this, "utf-8")


inline val String?.doubleValue: Double
    get() = if (TextUtils.isEmpty(this)) 0.0 else try {
        this!!.toDouble()
    } catch (e: Exception) {
        0.0
    }

inline val String?.intValue: Int
    get() = if (TextUtils.isEmpty(this)) 0 else try {
        this!!.toInt()
    } catch (e: Exception) {
        0
    }

inline val String?.floatValue: Float
    get() = if (TextUtils.isEmpty(this)) 0f else try {
        this!!.toFloat()
    } catch (e: Exception) {
        0f
    }

inline val CharSequence?.intValue: Int
    get() = toString().intValue

inline val CharSequence?.floatValue: Float
    get() = toString().floatValue


inline val String.isIp: Boolean
    get() {
        val p = Pattern.compile(
            "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"
        )
        val m = p.matcher(this)
        return m.find()
    }

fun stringPairOf(vararg pair: Pair<String, Any?>): String {
    val strList = ArrayList<String>(pair.size)
    for ((key, value) in pair) {
        strList.add("$key: $value")
    }
    return strList.joinToString()
}

const val NEW_LINE = "\n"

fun String.wrapInQuotes(): String {
    var formattedConfigString: String = this
    if (!startsWith("\"")) {
        formattedConfigString = "\"$formattedConfigString"
    }
    if (!endsWith("\"")) {
        formattedConfigString = "$formattedConfigString\""
    }
    return formattedConfigString
}

fun String.unwrapQuotes(): String {
    var formattedConfigString: String = this
    if (formattedConfigString.startsWith("\"")) {
        if (formattedConfigString.length > 1) {
            formattedConfigString = formattedConfigString.substring(1)
        } else {
            formattedConfigString = ""
        }
    }
    if (formattedConfigString.endsWith("\"")) {
        formattedConfigString = if (formattedConfigString.length > 1) {
            formattedConfigString.substring(0, formattedConfigString.length - 1)
        } else {
            ""
        }
    }
    return formattedConfigString
}


fun CharSequence.isEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.orDefault(defaultValue: CharSequence): CharSequence = if (isNullOrBlank()) defaultValue else this!!


fun String?.urlEncode(charsetName: String = "UTF-8"): String {
    if (this.isNullOrEmpty()) return ""
    try {
        return URLEncoder.encode(this, charsetName)
    } catch (e: UnsupportedEncodingException) {
        throw AssertionError(e)
    }
}

fun String?.urlDecode(charsetName: String = "UTF-8"): String {
    if (this.isNullOrEmpty()) return ""
    try {
        return URLDecoder.decode(this, charsetName)
    } catch (e: UnsupportedEncodingException) {
        throw AssertionError(e)
    }
}

fun CharSequence?.isMatch(regex: String): Boolean {
    return !this.isNullOrEmpty() && Regex(regex).matches(this)
}

fun CharSequence.setBackgroundColor(color: Int): CharSequence {
    val s = SpannableString(this)
    s.setSpan(BackgroundColorSpan(color), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return s
}

fun CharSequence.setForegroundColor(color: Int): CharSequence {
    val s = SpannableString(this)
    s.setSpan(ForegroundColorSpan(color), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return s
}

fun String.ifBlank(mapper: () -> String): String =
    ifBlank { mapper() }

fun String.ifEmpty(mapper: () -> String): String =
    ifEmpty { mapper() }

fun String?.ifNull(mapper: () -> String): String =
    this ?: mapper()

fun String.isHttp(): Boolean {
    return this.matches(Regex("(http|https)://[^\\s]*"))
}

inline fun <reified T : Enum<T>> String.toEnum(by: (enum: T) -> String = { it.name }): T? {
    return enumValues<T>().firstOrNull { by(it) == this }
}
