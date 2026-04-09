package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        experimentalProperties["android.experimental.enableScreenshotTest"] = true
    }

    dependencies {
        implementation(this@configureAndroidCompose.platform("androidx-compose-bom"))
        implementation(library("androidx-ui"))
        implementation(library("androidx-ui-graphics"))
        implementation(library("androidx-ui-tooling-preview"))
        implementation(library("androidx-material3"))
        implementation(library("androidx-lifecycle-runtime-ktx"))
        implementation(library("androidx-activity-compose"))
        implementation(library("androidx-navigation3-ui"))
        implementation(library("androidx-navigation3-runtime"))
        implementation(library("androidx-lifecycle-viewmodel-navigation3"))
        implementation(library("koin-compose"))
        implementation(library("koin-compose-viewmodel"))

        androidTestImplementation( this@configureAndroidCompose.platform("androidx-compose-bom"))
        androidTestImplementation( library("androidx-ui-test-junit4"))

        debugImplementation( library("androidx-ui-tooling"))
        debugImplementation( library("androidx-ui-test-manifest"))

        screenshotTestImplementation( library("screenshot-validation-api"))
        screenshotTestImplementation( library("androidx-ui-tooling"))

    }
}
