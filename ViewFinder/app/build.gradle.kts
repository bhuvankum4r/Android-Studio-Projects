plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.viewfinder"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.viewfinder"
        minSdk = 35
        targetSdk = 36
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    val camerax_version = "1.3.3" // latest stable as of now

    // CameraX core library
    implementation("androidx.camera:camera-core:$camerax_version")

    // CameraX Camera2 implementation
    implementation("androidx.camera:camera-camera2:$camerax_version")

    // Lifecycle for CameraX
    implementation("androidx.camera:camera-lifecycle:$camerax_version")

    // For preview use case
    implementation("androidx.camera:camera-view:$camerax_version")

    // For capturing images
    implementation("androidx.camera:camera-extensions:$camerax_version")

    // For saving images
    implementation("androidx.camera:camera-video:$camerax_version")

    // Glide (optional, for image preview)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // ML Kit Face Detection
    implementation("com.google.mlkit:face-detection:16.1.7")
// Glide (optional for preview)
    implementation("com.github.bumptech.glide:glide:4.16.0")
// (Optional) For bitmap blur (if you want advanced blur)
    implementation("jp.wasabeef:blurry:4.0.1")


}