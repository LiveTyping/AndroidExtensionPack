package ru.livetyping.extensionpack

import android.content.Context
import android.widget.Toast

object AndroidExtensionPack {

    fun makeToast(context: Context) {
        Toast.makeText(context, "Greeting", Toast.LENGTH_SHORT).show()
    }
}