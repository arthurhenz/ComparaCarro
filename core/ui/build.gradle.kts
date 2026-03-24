plugins {
    id("comparacarro.android.library")
    id("comparacarro.android.compose")
    id("comparacarro.koin")
    id("comparacarro.detekt")
}

android {
    namespace = "com.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.koin.compose.viewmodel)
}
