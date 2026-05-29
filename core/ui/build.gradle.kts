plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.comparacarro.android.compose)
    alias(libs.plugins.comparacarro.koin)
}

android {
    namespace = "com.ui"
}

dependencies {
    implementation(libs.material)
}
