plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.comparacarro.koin)
}

android {
    namespace = "com.data"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(projects.core.common)
    implementation(projects.network)

    implementation(libs.androidx.datastore.preferences)
}
