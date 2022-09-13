package ru.livetyping.extensionpack.view

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun List<View>.invisible() {
    this.forEach { it.invisible() }
}

fun List<View>.visible() {
    this.forEach { it.visible() }
}

inline fun View.ifVisible(action: () -> Unit) {
    if (isVisible) action()
}


inline fun View.ifInvisible(action: () -> Unit) {
    if (isInvisible) action()
}


inline fun View.ifGone(action: () -> Unit) {
    if (isGone) action()
}

fun View.showIf(boolean: Boolean, makeInvisible: Boolean = false) {
    visibility = if (boolean) View.VISIBLE else if (makeInvisible) View.INVISIBLE else View.GONE
}

fun View.enableIf(boolean: Boolean) = run { isEnabled = boolean }

fun View.disableIf(boolean: Boolean) = run { isEnabled = boolean.not() }

fun View.hideIf(value: Boolean) {
    visibility = if (!value) View.VISIBLE else View.INVISIBLE
}