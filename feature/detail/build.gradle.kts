plugins {
    alias(libs.plugins.app.library)
    id("comparacarro.android.compose")
    id("comparacarro.koin")
    id("comparacarro.detekt")
}

android {
    namespace = "com.comparacarro"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
}
