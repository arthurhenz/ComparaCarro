plugins {
    alias(libs.plugins.app.library)
    id("comparacarro.android.compose")
    id("comparacarro.koin")
    id("comparacarro.detekt")
}

android {
    namespace = "com.ui"
}

dependencies {
    implementation(libs.material)
}
