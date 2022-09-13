package ru.livetyping.extensionpack.view

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.StyleRes

fun TextView.setDifferentSizesText(
    text: String,
    @DimenRes differentTextSize: Int,
    differentSizeTextLength: Int,
    startPosition: Int = 0
) {
    val spannable = SpannableString(text)
    spannable.setSpan(
        AbsoluteSizeSpan(resources.getDimensionPixelSize(differentTextSize)),
        startPosition,
        differentSizeTextLength,
        0
    )
    this.text = spannable
}

var TextView.drawableStart: Drawable?
    get() = drawables[0]
    set(value) = setDrawables(value, drawableTop, drawableEnd, drawableBottom)

var TextView.drawableTop: Drawable?
    get() = drawables[1]
    set(value) = setDrawables(drawableStart, value, drawableEnd, drawableBottom)

var TextView.drawableEnd: Drawable?
    get() = drawables[2]
    set(value) = setDrawables(drawableStart, drawableTop, value, drawableBottom)

var TextView.drawableBottom: Drawable?
    get() = drawables[3]
    set(value) = setDrawables(drawableStart, drawableTop, drawableEnd, value)


private inline val TextView.drawables: Array<Drawable?>
    get() = compoundDrawablesRelative

private fun TextView.setDrawables(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
}

fun TextView.setAppearance(@StyleRes res: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextAppearance(res)
    } else {
        setTextAppearance(context, res)
    }
}