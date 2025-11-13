import org.gradle.kotlin.dsl.implementation
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.appdistribution)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.comparacarro"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.comparacarro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        lint {
            abortOnError = true
            baseline = file("lint-baseline.xml")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "New release version for testing"
                // Add testers by email (comma-separated)
                testers = "your-tester@example.com"
                // Or use groups (comma-separated group aliases from Firebase console)
                // groups = "qa-team, android-testers"
            }
        }
        debug {
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "Debug build for testing"
                testers = "your-tester@example.com"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    implementation(project(":feature:home"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:comparison"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":network"))
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
