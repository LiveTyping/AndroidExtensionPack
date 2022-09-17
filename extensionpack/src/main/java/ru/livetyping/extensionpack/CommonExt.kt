package ru.livetyping.extensionpack

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun <T : Any> T?.orElse(item: T) =
    this ?: item

fun <T : CharSequence> T?.orElse(item: T) =
    if (!isNullOrBlank()) this else item

fun <T : Number> T?.orElse(number: T) =
    if (this != null && this != 0) this else number

fun <T> List<T>?.orElse(list: List<T>) =
    if (!isNullOrEmpty()) this else list


fun <K, V> Map<K, V>?.orElse(map: Map<K, V>) =
    if (!isNullOrEmpty()) this else map

inline fun Boolean.ifTrue(function: () -> Unit): Boolean {
    if (this) function()
    return this
}


inline fun Boolean.ifFalse(function: () -> Unit): Boolean {
    if (!this) function()
    return this
}

inline fun Boolean.ifTrue(falseFunction: () -> Unit = {}, trueFunction: () -> Unit): Boolean {
    if (this) trueFunction() else falseFunction()
    return this
}

inline fun Boolean.ifFalse(trueFunction: () -> Unit = {}, falseFunction: () -> Unit): Boolean {
    if (this) trueFunction() else falseFunction()
    return this
}

val Context.isOnline: Boolean
    get() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null) {
                    return networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                            networkInfo.type == ConnectivityManager.TYPE_VPN || networkInfo.type == ConnectivityManager.TYPE_ETHERNET)
                }
            } else {
                val network = cm.activeNetwork

                if (network != null) {
                    val nc = cm.getNetworkCapabilities(network)
                    return if (nc == null) {
                        false
                    } else {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    }
                }
            }
        }
        return false
    }

inline fun <T> T.alsoIfTrue(boolean: Boolean, block: (T) -> Unit): T {
    if (boolean) block(this)
    return this
}

inline fun <T> T.alsoIfFalse(boolean: Boolean, block: (T) -> Unit): T {
    if (!boolean) block(this)
    return this
}

inline fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    null
}

inline fun <T> tryOrElse(defaultValue: T, block: () -> T): T = tryOrNull(block)
    ?: defaultValue

inline fun tryOrElse(defaultBlock: () -> Unit = {}, block: () -> Unit) = try {
    block()
} catch (e: Exception) {
    defaultBlock()
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T = apply {
    if (condition) {
        block()
    }
}

inline fun <T, R> T.letIf(condition: Boolean, block: (T) -> R): R? = let {
    if (condition) {
        block(it)
    } else {
        null
    }
}

inline fun <T, R> T.runIf(condition: Boolean, block: T.() -> R): R? = run {
    if (condition) {
        block()
    } else {
        null
    }
}

inline fun <T> T.alsoIf(condition: Boolean, block: (T) -> T): T = also {
    if (condition) {
        block(it)
    }
}

inline fun <T, R> withIf(receiver: T, condition: Boolean, block: T.() -> R): R? = with(receiver) {
    if (condition) {
        block()
    } else {
        null
    }
}

fun <T, R> allIsNotNull(vararg values: T, out: () -> R?): R? {
    values.forEach {
        if (it == null) return null
    }
    return out()
}

fun <T> allIsEqual(vararg values: T): Boolean {
    when {
        values.isEmpty() -> return false
        values.size == 1 -> return true
    }
    values.forEach {
        if ((it == values.first()).not()) return false
    }
    return true
}