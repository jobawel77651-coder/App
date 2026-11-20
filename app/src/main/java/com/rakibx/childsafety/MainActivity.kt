package com.rakibx.childsafety

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.view.Gravity
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : Activity() {
    private val requestCode = 100
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 10, 25)
        window.navigationBarColor = Color.rgb(12, 10, 25)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
            setBackgroundColor(Color.rgb(12, 10, 25))
        }
        val title = TextView(this).apply {
            text = "RAKIBX"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        val sub = TextView(this).apply {
            text = "CHILD SAFETY • ${BuildConfig.DEVICE_ID.uppercase()}"
            textSize = 13f
            setTextColor(Color.rgb(183, 160, 255))
            gravity = Gravity.CENTER
        }
        status = TextView(this).apply {
            text = "Location sharing is OFF"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 28)
        }
        val start = makeButton("START LOCATION SHARING") { requestAndStart() }
        val stop = makeButton("STOP LOCATION SHARING") {
            stopService(Intent(this, LocationShareService::class.java))
            status.text = "Location sharing is OFF"
        }
        val permissionReport = makeButton("SEND PERMISSION STATUS") {
            Thread { ApiClient.sendPermissions(this) }.start()
            Toast.makeText(this, "Permission status report sent if internet is available", Toast.LENGTH_LONG).show()
        }
        val sos = makeButton("SEND SOS ALERT") {
            Thread { ApiClient.sendEvent("sos", "SOS requested from ${BuildConfig.DEVICE_ID}") }.start()
            Toast.makeText(this, "SOS request sent (if internet is available)", Toast.LENGTH_LONG).show()
        }
        val note = TextView(this).apply {
            text = "Sharing is visible and starts only when you tap START. Android keeps an ongoing notification while it is active."
            textSize = 12f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        listOf(title, sub, status, start, stop, permissionReport, sos, note).forEach { root.addView(it, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 14 }) }
        setContentView(root)
    }

    private fun makeButton(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        setOnClickListener { action() }
    }

    private fun requestAndStart() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= 33) permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        val missing = permissions.filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), requestCode)
        } else startSharing()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode && grantResults.isNotEmpty() && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) startSharing()
        else Toast.makeText(this, "Location permission is required to share location", Toast.LENGTH_LONG).show()
    }

    private fun startSharing() {
        val intent = Intent(this, LocationShareService::class.java)
        ContextCompat.startForegroundService(this, intent)
        status.text = "Location sharing is ON — visible notification active"
    }
}
