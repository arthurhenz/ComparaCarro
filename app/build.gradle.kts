plugins {
    alias(libs.plugins.app.application)
    id("comparacarro.android.compose")
    id("comparacarro.koin")
    id("comparacarro.detekt")
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
    implementation(project(":feature:home"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:comparison"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":network"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
