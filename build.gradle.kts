plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.rakibx.childsafety"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.rakibx.childsafety"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "DEVICE_ID", "\"device1\"")
        buildConfigField("String", "DEVICE_KEY", "\"CHANGE_ME_DEVICE_KEY\"")
        buildConfigField("String", "API_BASE_URL", "\"https://YOUR-DOMAIN.example\"")
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation("com.google.android.gms:play-services-location:21.3.0")
}
