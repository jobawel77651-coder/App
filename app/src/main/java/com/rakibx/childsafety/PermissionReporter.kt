package com.rakibx.childsafety

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import org.json.JSONObject

object PermissionReporter {
    private fun state(context: Context, permission: String, runtime: Boolean = true): String {
        val granted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        if (granted) return "ALLOWED"
        if (!runtime) return "NOT_GRANTED"
        if (Build.VERSION.SDK_INT < 23) return "NOT_GRANTED"
        val flags = try {
            context.packageManager.getPermissionFlags(permission, context.packageName, android.os.Process.myUserHandle())
        } catch (_: Exception) { 0 }
        val userSet = flags and PackageManager.FLAG_PERMISSION_USER_SET != 0
        val userFixed = flags and PackageManager.FLAG_PERMISSION_USER_FIXED != 0
        return if (userSet || userFixed) "DENIED" else "NOT_REQUESTED"
    }

    fun snapshot(context: Context): JSONObject = JSONObject().apply {
        put("location_fine", state(context, Manifest.permission.ACCESS_FINE_LOCATION))
        put("location_coarse", state(context, Manifest.permission.ACCESS_COARSE_LOCATION))
        put("camera", state(context, Manifest.permission.CAMERA))
        put("microphone", state(context, Manifest.permission.RECORD_AUDIO))
        put("sms", state(context, Manifest.permission.READ_SMS))
        put("call_log", state(context, Manifest.permission.READ_CALL_LOG))
        if (Build.VERSION.SDK_INT >= 33) {
            put("notifications", state(context, Manifest.permission.POST_NOTIFICATIONS))
        } else {
            put("notifications", "NOT_REQUIRED")
        }
    }
}
