package com.rakibx.childsafety

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    fun sendLocation(lat: Double, lon: Double, accuracy: Float, battery: Int) {
        val body = JSONObject().apply {
            put("device_id", Config.deviceId)
            put("lat", lat); put("lon", lon)
            put("accuracy", accuracy); put("battery", battery)
        }
        post("/api/location", body)
    }

    fun sendPermissions(context: android.content.Context) {
        val body = PermissionReporter.snapshot(context).apply {
            put("device_id", Config.deviceId)
        }
        post("/api/permissions", body)
    }

    fun sendEvent(type: String, message: String) {
        val body = JSONObject().apply {
            put("device_id", Config.deviceId)
            put("type", type); put("message", message)
        }
        post("/api/event", body)
    }

    private fun post(path: String, body: JSONObject) {
        try {
            val conn = (URL(Config.baseUrl + path).openConnection() as HttpURLConnection)
            conn.requestMethod = "POST"
            conn.connectTimeout = 10000
            conn.readTimeout = 10000
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("X-Device-Key", Config.deviceKey)
            conn.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            conn.inputStream.close()
            conn.disconnect()
        } catch (_: Exception) {
            // Network may be unavailable; no location is queued or secretly retried.
        }
    }
}
