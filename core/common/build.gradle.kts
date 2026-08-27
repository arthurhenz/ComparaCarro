plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.comparacarro.android.compose)
    alias(libs.plugins.comparacarro.koin)
}

android {
    namespace = "com.common"
}

dependencies {
    implementation(libs.androidx.compose.foundation)
}
