plugins {
    alias(libs.plugins.app.library)
    id("comparacarro.koin")
}

android {
    namespace = "com.data"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(":core:common"))
    implementation(project(":network"))

    implementation(libs.androidx.datastore.preferences)
}
