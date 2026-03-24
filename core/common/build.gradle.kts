plugins {
    id("comparacarro.android.library")
    id("comparacarro.detekt")
}

android {
    namespace = "com.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
