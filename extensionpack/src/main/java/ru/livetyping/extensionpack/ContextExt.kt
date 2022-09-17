package ru.livetyping.extensionpack

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import androidx.annotation.PluralsRes
import androidx.core.content.ContextCompat
import java.util.*

fun Context.isPermissionsNotGranted(list: Array<String>) =
    list.map { ContextCompat.checkSelfPermission(this, it) }
        .firstOrNull { result -> result != PackageManager.PERMISSION_GRANTED }

fun Context.makeCall(number: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.showDatePicker(year: Int, month: Int, day: Int, onDatePicked: (year: Int, month: Int, day: Int) -> Unit) {
    DatePickerDialog(this, { _, pyear, pmonth, pdayOfMonth ->
        onDatePicked(pyear, pmonth, pdayOfMonth)
    }, year, month, day).show()
}

fun Context.showTimePicker(
    currentDate: Date,
    is24Hour: Boolean = false,
    onDatePicked: (hour: Int, minute: Int) -> Unit
) {
    @Suppress("DEPRECATION")
    TimePickerDialog(this, { _, hourOfDay, minute ->
        onDatePicked(hourOfDay, minute)

    }, currentDate.hours, currentDate.minutes, is24Hour).show()
}

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any?) =
    if (this == null) "" else resources.getQuantityString(res, quantity, *args)

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int) = quantityString(res, quantity, quantity)

val Context.isDarkTheme
    get() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO,
        Configuration.UI_MODE_NIGHT_UNDEFINED -> false

        else -> true
    }

fun Context.rateUs() {
    try {
        startActivity(Intent("android.intent.action.VIEW", Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}