plugins {
    id("comparacarro.android.library")
    id("comparacarro.detekt")
}

android {
    namespace = "com.example.utils"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
