plugins {
    alias(libs.plugins.app.library)
    id("comparacarro.detekt")
}

android {
    namespace = "com.example.utils"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
