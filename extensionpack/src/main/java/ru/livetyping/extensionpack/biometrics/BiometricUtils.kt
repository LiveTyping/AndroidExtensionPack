package ru.livetyping.extensionpack.biometrics

import androidx.biometric.BiometricManager.*
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors

inline fun FragmentActivity.biometricAuth(
    promptInfo: BiometricPrompt.PromptInfo,
    crossinline onAuthFailed: () -> Unit,
    crossinline onAuthError: (errorCode: Int, errorMessage: String) -> Unit = { _, _ -> },
    crossinline onAuthSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit = { _ -> }
): BiometricPrompt {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onAuthError(errorCode, errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthSuccess(result)
        }
    })

    biometricPrompt.authenticate(promptInfo)
    return biometricPrompt
}

inline fun Fragment.biometricAuth(
    promptInfo: BiometricPrompt.PromptInfo,
    crossinline onAuthFailed: () -> Unit,
    crossinline onAuthError: (errorCode: Int, errorMessage: String) -> Unit = { _, _ -> },
    crossinline onAuthSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit = { _ -> }
): BiometricPrompt {
    val executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onAuthError(errorCode, errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthSuccess(result)
        }
    })

    biometricPrompt.authenticate(promptInfo)
    return biometricPrompt
}

inline fun FragmentActivity.canAuthenticate(
    type: Int = DEVICE_CREDENTIAL or BIOMETRIC_STRONG,
    hardwareUnavailable: () -> Unit = {},
    securityUpdateNeeded: () -> Unit = {},
    noFingerprintsEnrolled: () -> Unit = {}, canAuthenticateAction: () -> Unit = {}
) {
    when (from(this).canAuthenticate(type)) {
        BIOMETRIC_SUCCESS -> {
            canAuthenticateAction()
        }

        BIOMETRIC_ERROR_NONE_ENROLLED -> {
            noFingerprintsEnrolled()
        }

        BIOMETRIC_ERROR_NO_HARDWARE -> {
            hardwareUnavailable()
        }

        BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            hardwareUnavailable()
        }

        BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            securityUpdateNeeded()
        }

        BIOMETRIC_ERROR_UNSUPPORTED -> {
            hardwareUnavailable()
        }

        BIOMETRIC_STATUS_UNKNOWN -> {
            hardwareUnavailable()
        }
    }
}

inline fun Fragment.canAuthenticate(
    type: Int = DEVICE_CREDENTIAL or BIOMETRIC_STRONG,
    hardwareUnavailable: () -> Unit = {},
    securityUpdateNeeded: () -> Unit = {},
    noFingerprintsEnrolled: () -> Unit = {}, canAuthenticateAction: () -> Unit = {}
) {
    when (from(requireContext()).canAuthenticate(type)) {
        BIOMETRIC_SUCCESS -> {
            canAuthenticateAction()
        }

        BIOMETRIC_ERROR_NONE_ENROLLED -> {
            noFingerprintsEnrolled()
        }

        BIOMETRIC_ERROR_NO_HARDWARE -> {
            hardwareUnavailable()
        }

        BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            hardwareUnavailable()
        }

        BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            securityUpdateNeeded()
        }

        BIOMETRIC_ERROR_UNSUPPORTED -> {
            hardwareUnavailable()
        }

        BIOMETRIC_STATUS_UNKNOWN -> {
            hardwareUnavailable()
        }
    }
}