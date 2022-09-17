package ru.livetyping.extensionpack.strings

import android.text.Spannable
import android.text.style.StyleSpan
import androidx.annotation.StyleRes

fun String.toBoolean(): Boolean {
    return this != "" &&
            (this.equals("TRUE", ignoreCase = true)
                    || this.equals("Y", ignoreCase = true)
                    || this.equals("YES", ignoreCase = true))
}

fun String.convertToCamelCase(): String {
    var titleText = ""
    if (this.isNotEmpty()) {
        val words = this.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        words.filterNot { it.isEmpty() }
            .map { it.substring(0, 1).uppercase() + it.substring(1).lowercase() }
            .forEach { titleText += "$it " }
    }
    return titleText.trim { it <= ' ' }
}

fun String.ellipsize(at: Int): String {
    if (this.length > at) {
        return this.substring(0, at) + "..."
    }
    return this
}

fun Spannable.highlightSubstring(query: String, @StyleRes style: Int): Spannable {
    val spannable = Spannable.Factory.getInstance().newSpannable(this)
    val substrings = query.lowercase().split("\\s+".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
    var lastIndex = 0
    for (i in substrings.indices) {
        do {
            lastIndex = this.toString().lowercase().indexOf(substrings[i], lastIndex)
            if (lastIndex != -1) {
                spannable.setSpan(
                    StyleSpan(style),
                    lastIndex,
                    lastIndex + substrings[i].length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                lastIndex++
            }
        } while (lastIndex != -1)
    }
    return spannable
}

inline fun <reified T : Enum<T>> String.toEnum(by: (enum: T) -> String = { it.name }): T? {
    return enumValues<T>().firstOrNull { by(it) == this }
}

fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}

fun String.capitalizeFirstLetterEachWord(): String {
    return this.lowercase()
        .split(" ")
        .joinToString(" ") { it.capitalize() }
}