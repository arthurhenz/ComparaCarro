plugins {
    id("comparacarro.android.application")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(project(":feature:home"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:comparison"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":network"))

    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
