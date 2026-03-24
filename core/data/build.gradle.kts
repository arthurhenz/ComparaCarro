plugins {
    id("comparacarro.android.library")
    id("comparacarro.koin")
    id("comparacarro.detekt")
}

android {
    namespace = "com.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(":core:common"))
    implementation(project(":network"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.koin.compose.viewmodel)
    implementation(libs.androidx.datastore.preferences)
}
