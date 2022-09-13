package ru.livetyping.extensionpack.view

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.tabs.TabLayout
import ru.livetyping.extensionpack.dpToPx
import java.time.LocalDate

const val FAST_CLICK_DELAY = 500L

fun View.addKeyBoardStateCallback(isOpened: (Boolean) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val heightDiff = rootView.height - height
        isOpened(heightDiff > 200.dpToPx())
    }
}

fun View.setOnClickListenerWithTimeout(callback: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime > FAST_CLICK_DELAY) {
            lastClickTime = SystemClock.elapsedRealtime()
            callback.invoke()
        }
    }
}

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null,
) {
    (this.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
        left?.let(it::setMarginStart)
        right?.let(it::setMarginEnd)
        it.topMargin = top ?: it.topMargin
        it.bottomMargin = bottom ?: it.bottomMargin
        requestLayout()
    }
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.tintBackgroundDrawable(@ColorRes colorRes: Int) {
    val drawable = DrawableCompat.wrap(background)
    drawable.mutate()
    DrawableCompat.setTint(drawable, getColor(colorRes))
}

fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable {
    val drawable = DrawableCompat.wrap(this)
    drawable.mutate()
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, color))
    return this
}

fun View.getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(context, colorRes)

inline fun TabLayout.doOnTabSelected(
    crossinline action: (tab: TabLayout.Tab?) -> Unit
) = addOnTabSelectedListener(onTabSelected = action)

inline fun TabLayout.addOnTabSelectedListener(
    crossinline onTabSelected: (tab: TabLayout.Tab?) -> Unit = { _ -> },
    crossinline onTabUnselected: (tab: TabLayout.Tab?) -> Unit = { _ -> },
    crossinline onTabReselected: (tab: TabLayout.Tab?) -> Unit = { _ -> },
): TabLayout.OnTabSelectedListener {
    val listener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            onTabSelected.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onTabUnselected.invoke(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            onTabReselected.invoke(tab)
        }
    }
    addOnTabSelectedListener(listener)
    return listener
}

fun View.setGradientDrawable(
    @ColorRes startColorRes: Int,
    @ColorRes endColorRes: Int,
    orientation: GradientDrawable.Orientation,
    radius: Float = 0F
) {
    setGradientDrawableFromColor(
        intArrayOf(
            ContextCompat.getColor(context, startColorRes),
            ContextCompat.getColor(context, endColorRes)
        ),
        orientation,
        radius
    )
}

fun View.setGradientDrawableFromColor(
    @ColorInt colors: IntArray,
    orientation: GradientDrawable.Orientation,
    radius: Float = 0F
) {
    background = getGradientDrawable(colors, orientation, radius)
}

fun View.getGradientDrawable(
    @ColorInt colors: IntArray,
    orientation: GradientDrawable.Orientation,
    radius: Float = 0F
) = GradientDrawable(orientation, colors).apply { cornerRadius = radius }

@Nullable
fun getDrawableResIdByName(context: Context, drawableName: String) =
    context.resources.getIdentifier(drawableName, "drawable", context.packageName)

fun View.getDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(context, drawableRes)

fun View.getString(@StringRes res: Int) = resources.getString(res)

@RequiresApi(Build.VERSION_CODES.O)
fun View.showDatePickerDialog(_date: LocalDate?, callback: DateDialogCallback) {
    val date = _date ?: LocalDate.now()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth -> callback.invoke(LocalDate.of(year, month + 1, dayOfMonth)) },
        date.year,
        date.month.ordinal,
        date.dayOfMonth
    ).apply {
        datePicker.maxDate = System.currentTimeMillis()
    }.show()
}
typealias DateDialogCallback = (LocalDate) -> Unit

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    Canvas(bitmap).apply {
        setBounds(0, 0, width, height)
        draw(this)
    }
    return bitmap
}