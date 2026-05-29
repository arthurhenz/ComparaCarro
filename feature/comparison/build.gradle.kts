plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.comparacarro.android.compose)
    alias(libs.plugins.comparacarro.koin)
}

android {
    namespace = "com.comparacarro"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
}
