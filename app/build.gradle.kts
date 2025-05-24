plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.senemarketkotlin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.senemarketkotlin"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Firebase BOM (define versión única para todas las libs Firebase)
    implementation(platform(libs.firebase.bom))

    // Firebase
    implementation(libs.firebase.messaging)       // Firebase Cloud Messaging
    implementation(libs.firebase.auth)            // Authentication
    implementation(libs.firebase.crashlytics)     // Crashlytics
    implementation(libs.firebase.firestore.ktx)   // Firestore
    implementation("com.google.firebase:firebase-storage") // Firebase Storage (no disponible como `libs` aún)

    // AndroidX, Jetpack Compose & Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.room.ktx)

    // Compose UI & Material
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Exoplayer
    implementation(libs.androidx.media3.exoplayer)

    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    // Coil (image loading)
    implementation(libs.coil)
    implementation("io.coil-kt.coil3:coil-compose:3.1.0") // Nueva versión de Coil para Compose

    // Tests

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}