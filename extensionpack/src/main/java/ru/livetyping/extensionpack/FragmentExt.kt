package ru.livetyping.extensionpack

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.livetyping.extensionpack.view.DateDialogCallback
import ru.livetyping.extensionpack.view.getColor
import ru.livetyping.extensionpack.view.getDrawable
import ru.livetyping.extensionpack.view.showDatePickerDialog
import java.io.File
import java.io.InputStream
import java.time.LocalDate

fun Fragment.shareText(text: String, chooserMessage: String? = null) =
    requireActivity().shareText(text, chooserMessage)

fun Fragment.shareImage(image: Bitmap, text: String?, chooserMessage: String? = null) =
    requireActivity().shareImage(image, text, chooserMessage)

fun Fragment.shareMedia(file: File, text: String?, chooserMessage: String? = null) =
    requireActivity().shareMedia(file, text, chooserMessage)

fun Fragment.shareMedia(uri: Uri, text: String?, chooserMessage: String? = null) =
    requireActivity().shareMedia(uri, text, chooserMessage)

fun Fragment.openBrowser(url: String) = requireActivity().openBrowser(url)

fun Fragment.openMailClient(email: String, subject: String? = null, text: String? = null) =
    requireActivity().openMailClient(email, subject, text)

fun Fragment.setDarkStatusBarIcons(@ColorRes statusColorId: Int) =
    requireActivity().setDarkStatusBarIcons(statusColorId)

fun Fragment.setLightStatusBarIcons() = requireActivity().setLightStatusBarIcons()

fun Fragment.getPhotoFromGallery() = requireActivity().getPhotoFromGallery()

fun Fragment.getPhotoFromCamera() = requireActivity().getPhotoFromCamera()

fun Fragment.isPermissionsNotGranted(list: Array<String>) = requireContext().isPermissionsNotGranted(list)

fun Fragment.getColor(@ColorRes colorRes: Int) = requireView().getColor(colorRes)

fun Fragment.getDrawable(@DrawableRes drawableRes: Int) = requireView().getDrawable(drawableRes)

fun Fragment.registerRequestContentImage(callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        val imageStream: InputStream = requireActivity()
            .contentResolver
            .openInputStream(uri) ?: return@registerForActivityResult
        BitmapFactory.decodeStream(imageStream).let(callback::invoke)
    }

fun Fragment.registerRequestContentMultipleImages(callback: (List<Bitmap>) -> Unit) =
    registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList: List<Uri>? ->
        uriList ?: return@registerForActivityResult
        val items = uriList.map {
            val imageStream: InputStream = requireActivity()
                .contentResolver
                .openInputStream(it) ?: return@registerForActivityResult
            BitmapFactory.decodeStream(imageStream)
        }
        callback.invoke(items)
    }

fun Fragment.registerRequestContentVideo(callback: (Uri) -> Unit) =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        callback.invoke(uri)
    }


fun Fragment.registerRequestTakePhoto(uri: Uri, callback: (Bitmap) -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            val imageStream: InputStream = requireActivity()
                .contentResolver
                .openInputStream(uri) ?: return@registerForActivityResult
            BitmapFactory.decodeStream(imageStream).let(callback::invoke)
        }
    }

fun Fragment.registerRequestTakeVideo(callback: () -> Unit) =
    registerForActivityResult(ActivityResultContracts.TakeVideo()) { _ ->
        callback.invoke()
    }

fun Fragment.getUriForImageFile() = requireActivity().getUriForImageFile()

fun Fragment.getUriForVideoFile() = requireActivity().getUriForVideoFile()

fun Fragment.setSoftMode(mode: SoftInputMode) {
    val mod = when (mode) {
        SoftInputMode.RESIZE -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        SoftInputMode.NOTHING -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        SoftInputMode.PAN -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        SoftInputMode.HIDDEN -> WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
    }
    activity?.window?.setSoftInputMode(mod)
}

fun Fragment.getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

fun Fragment.getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

enum class SoftInputMode {
    RESIZE, NOTHING, PAN, HIDDEN
}

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.showDateDialog(date: LocalDate?, callback: DateDialogCallback) {
    requireView().showDatePickerDialog(date, callback)
}