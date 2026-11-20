package com.rakibx.childsafety

object Config {
    // Set these through Gradle BuildConfig fields for each phone.
    val baseUrl: String get() = BuildConfig.API_BASE_URL.trimEnd('/')
    val deviceId: String get() = BuildConfig.DEVICE_ID
    val deviceKey: String get() = BuildConfig.DEVICE_KEY
}
