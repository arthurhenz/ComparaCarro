package extensions

import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKtlint() {
    pluginManager.apply("org.jlleitschuh.gradle.ktlint")

    extensions.configure<KtlintExtension> {
        android.set(true)
    }
}
