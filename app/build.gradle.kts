plugins {
    alias(libs.plugins.app.application)
    alias(libs.plugins.comparacarro.android.compose)
    alias(libs.plugins.comparacarro.koin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.comparacarro"

    defaultConfig {
        applicationId = "com.comparacarro"
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
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.detail)
    implementation(projects.feature.comparison)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
