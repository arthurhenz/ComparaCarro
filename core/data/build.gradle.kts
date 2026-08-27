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

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging.runtime)
}
