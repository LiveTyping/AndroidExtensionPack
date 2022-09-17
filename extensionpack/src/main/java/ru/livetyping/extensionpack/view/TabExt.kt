package ru.livetyping.extensionpack.view

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

operator fun TabLayout.get(position: Int): TabLayout.Tab = getTabAt(position)!!

inline fun TabLayout.forEach(func: (TabLayout.Tab) -> Unit) {
    for (i in 0 until tabCount) func(get(i))
}

fun TabLayout.tint(
    selectedPosition: Int = 0,
    selectedColor: Int = ContextCompat.getColor(context, android.R.color.white),
    defaultColor: Int = Color.parseColor("#80FFFFFF")
) {
    forEach { it.icon?.setTint(defaultColor) }
    get(selectedPosition).icon?.setTint(selectedColor)
}

fun TabLayout.hideTitles() = forEach {
    it.contentDescription = it.text
    it.text = ""
}

fun TabLayout.setIcons(icons: List<Drawable>) {
    for (i in 0 until tabCount) get(i).icon = icons[i]
    tint()
}

fun TabLayout.setIcons(@DrawableRes icons: Array<Int>) {
    for (i in 0 until tabCount) get(i).icon = ContextCompat.getDrawable(context, icons[i])
    tint()
}