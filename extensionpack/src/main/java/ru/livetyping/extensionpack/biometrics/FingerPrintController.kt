package ru.livetyping.extensionpack.biometrics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import java.io.IOException
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.security.cert.CertificateException

@RequiresApi(Build.VERSION_CODES.M)
class FingerPrintController(
    private val fingerprintManagerCompat: FingerprintManagerCompat,
    private val callback: FingerprintCallback,
    private val context: Context,
    private val DEFAULT_KEY_NAME: String = "crypto-default_key"
) : FingerprintManagerCompat.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled = false

    private var cryptoObject: FingerprintManagerCompat.CryptoObject? = null
    private var keyStore: KeyStore? = null
    private var keyGenerator: KeyGenerator? = null

    private val isFingerPrintAuthAvailable: Boolean
        get() = fingerprintManagerCompat.isHardwareDetected && fingerprintManagerCompat.hasEnrolledFingerprints()

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject) {
        if (!isFingerPrintAuthAvailable) return

        cancellationSignal = CancellationSignal()

        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)

    }

    fun initialize() {
        if (isFingerPrintAuthAvailable) {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore")
            } catch (e: KeyStoreException) {
                throw RuntimeException("Failed to get an instance of KeyStore", e)
            }

            try {
                keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to get an instance of KeyGenerator", e)
            } catch (e: NoSuchProviderException) {
                throw RuntimeException("Failed to get an instance of KeyGenerator", e)
            }

            createKey(DEFAULT_KEY_NAME, false)

            val defaultCipher: Cipher
            try {
                defaultCipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to get an instance of Cipher", e)
            } catch (e: NoSuchPaddingException) {
                throw RuntimeException("Failed to get an instance of Cipher", e)
            }

            if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {
                cryptoObject = FingerprintManagerCompat.CryptoObject(defaultCipher)
            }
        }
    }

    private fun initCipher(cipher: Cipher, keyName: String): Boolean {
        try {
            keyStore?.load(null)
            val key = keyStore?.getKey(keyName, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    private fun createKey(keyName: String, invalidatedByBiometricEnrollment: Boolean) {
        try {
            keyStore?.load(null)

            val builder = KeyGenParameterSpec.Builder(
                keyName,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
            }
            keyGenerator?.init(builder.build())
            keyGenerator?.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun stopListening() {
        cancellationSignal?.let {
            selfCancelled = true
            it.cancel()
            cancellationSignal = null
        }
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {

        if (selfCancelled) {

            callback.onSelfCancelled()

        } else {
            callback.onError()
            Toast.makeText(context, errString.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun resumeListening() {
        cryptoObject?.let {
            startListening(it)
        }
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        val vibration = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        callback.onAuthenticated()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibration.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibration.vibrate(30)
        }
    }

    interface FingerprintCallback {
        fun onAuthenticated()
        fun onError()
        fun onSelfCancelled()
    }
}