package ru.livetyping.extensionpack.toasts

import androidx.annotation.StringRes

interface ToasterContract {

    fun shortToast(@StringRes string: Int)

    fun shortToast(string: String)

    fun longToast(@StringRes string: Int)

    fun longToast(string: String)
}