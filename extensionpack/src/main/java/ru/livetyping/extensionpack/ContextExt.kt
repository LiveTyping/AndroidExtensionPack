package ru.livetyping.extensionpack

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.*

fun Context.isPermissionsNotGranted(list: Array<String>) =
    list.map { ContextCompat.checkSelfPermission(this, it) }
        .firstOrNull { result -> result != PackageManager.PERMISSION_GRANTED }

fun Context.openMap(lat: String, lon: String, address: String) {
    val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$address")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    startActivity(mapIntent)
}

fun Context.routeWithGoogleMaps(startLocation: Location, endLocation: Location) {
    val intentUri = Uri.parse(
        "http://maps.google.com/maps?f=d&hl=en&saddr=" +
                "${startLocation.latitude}," +
                "${startLocation.longitude}" +
                "&daddr=" +
                "${endLocation.latitude}," +
                "${endLocation.longitude}"
    )
    val googleMapIntent = Intent(Intent.ACTION_VIEW, intentUri)
    startActivity(googleMapIntent)
}

fun Context.makeCall(number: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.registerDownloadSuccessListener(downloadId: Long, @StringRes downloadMessageSuccessful: Int) {
    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == id) {
                Toast.makeText(
                    context,
                    getString(downloadMessageSuccessful),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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

fun Context.watchYoutubeVideo(id: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://www.youtube.com/watch?v=$id")
    )
    try {
        this.startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        this.startActivity(webIntent)
    }
}

fun Context.areNotificationsEnabled(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int, vararg args: Any?) =
    if (this == null) "" else resources.getQuantityString(res, quantity, *args)

fun Context?.quantityString(@PluralsRes res: Int, quantity: Int) = quantityString(res, quantity, quantity)

fun Context.uriFromResource(@DrawableRes resId: Int): String = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(resources.getResourcePackageName(resId))
    .appendPath(resources.getResourceTypeName(resId))
    .appendPath(resources.getResourceEntryName(resId))
    .build().toString()

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

fun Context.shortToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.longToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

fun Context.showToastHard(msg: String) = AlertDialog.Builder(this).setMessage(msg).show()

fun Context.showConfirmationDialog(
    msg: String,
    onResponse: (result: Boolean) -> Unit,
    positiveText: String = "Yes",
    negativeText: String = "No",
    cancelable: Boolean = false
) =
    AlertDialog.Builder(this).setMessage(msg).setPositiveButton(positiveText) { _, _ -> onResponse(true) }
        .setNegativeButton(
            negativeText
        ) { _, _ -> onResponse(false) }.setCancelable(cancelable).show()

fun Context.showSinglePicker(choices: Array<String>, onResponse: (index: Int) -> Unit, checkedItemIndex: Int = -1) =
    AlertDialog.Builder(this).setSingleChoiceItems(choices, checkedItemIndex) { dialog, which ->
        onResponse(which)
        dialog.dismiss()
    }.show()

fun Context.showMultiPicker(
    choices: Array<String>,
    onResponse: (index: Int, isChecked: Boolean) -> Unit,
    checkedItems: BooleanArray? = null
) =
    AlertDialog.Builder(this).setMultiChoiceItems(choices, checkedItems) { _, which, isChecked ->
        onResponse(
            which,
            isChecked
        )
    }.setPositiveButton("Done", null).show()