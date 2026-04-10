plugins {
    alias(libs.plugins.app.library)
}

android {
    namespace = "com.example.utils"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
