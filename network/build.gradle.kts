plugins {
    alias(libs.plugins.app.library)
    id("comparacarro.koin")
    id("comparacarro.detekt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "comparacarro.network"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
}
