plugins {
    id("comparacarro.android.library")
    id("comparacarro.detekt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.navigation3.runtime)
    implementation(projects.core.common) // COLOCAR OS NAVIGATION NO MODULO NAVIGATION
}
