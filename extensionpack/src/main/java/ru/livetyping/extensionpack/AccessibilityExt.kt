package ru.livetyping.extensionpack

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf

inline fun <reified T : AccessibilityService> Context.hasAccessibilityPermission(): Boolean {
    val expectedComponentName = ComponentName(this, T::class.java)
    val enabledServicesSetting =
        Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            ?: return false
    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    colonSplitter.setString(enabledServicesSetting)
    while (colonSplitter.hasNext()) {
        val componentNameString = colonSplitter.next()
        val enabledService = ComponentName.unflattenFromString(componentNameString)
        if (enabledService != null && enabledService == expectedComponentName) return true
    }
    return false
}


val Context.isAccessibilityEnabled get() = getSystemService<AccessibilityManager>()?.isEnabled ?: false

inline fun <reified T : AccessibilityService> Context.isAccessibilityServiceRunning(): Boolean {
    val settingsString = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    return settingsString != null && settingsString.contains("${packageName}/${T::class.java.name}")
}

fun Context.askForAccessibilityPermission() = startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

inline fun <reified T> Context.askForAccessibilityPermissionHighlight(
    argKey: String = ":settings:fragment_args_key",
    showFragsKey: String = ":settings:show_fragment_args"
) {
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        val showArgs = packageName + "/" + T::class.java.name
        putExtra(argKey, showArgs)
        putExtra(showFragsKey, bundleOf(argKey to showArgs))
    })
}