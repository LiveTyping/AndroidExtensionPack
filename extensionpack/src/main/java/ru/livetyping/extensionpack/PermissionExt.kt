package ru.livetyping.extensionpack

import android.Manifest.permission.*
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun Fragment.askForMultiplePermissions(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
): ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result.map { it.value }.filter { it == false }
        if (granted.isEmpty()) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun FragmentActivity.askForMultiplePermissions(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
): ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val granted = result.map { it.value }.filter { it == false }
        if (granted.isEmpty()) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun Fragment.askForSinglePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
): ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun FragmentActivity.askForSinglePermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onPermissionsGranted: () -> Unit = {}
): ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            onPermissionsGranted()
        } else {
            onDenied()
        }
    }

inline fun Fragment.getForegroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
): Unit =
    askForMultiplePermissions(onDenied, onLocationGranted).launch(
        arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    )

inline fun FragmentActivity.getForegroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    askForMultiplePermissions(onDenied, onLocationGranted).launch(
        arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    )

inline fun Fragment.getBackgroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    when {
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
        }

        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
            getForegroundLocationPermission(onDenied, onLocationGranted)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            getForegroundLocationPermission(onDenied) {
                askForSinglePermission(onDenied, onLocationGranted).launch(
                    ACCESS_BACKGROUND_LOCATION
                )
            }
        }

        else -> {}
    }

inline fun FragmentActivity.getBackgroundLocationPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onLocationGranted: () -> Unit = {}
) =
    when {
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
        }

        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
            getForegroundLocationPermission(onDenied, onLocationGranted)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            getForegroundLocationPermission(onDenied) {
                askForSinglePermission(onDenied, onLocationGranted).launch(
                    ACCESS_BACKGROUND_LOCATION
                )
            }
        }

        else -> {}
    }

inline fun Fragment.getCameraPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onGranted: () -> Unit = {}
) =
    askForSinglePermission(onDenied, onGranted).launch(CAMERA)


inline fun FragmentActivity.getCameraPermission(
    crossinline onDenied: () -> Unit = {},
    crossinline onGranted: () -> Unit = {}
) = askForSinglePermission(onDenied, onGranted).launch(CAMERA)