import com.android.build.api.dsl.CommonExtension
import com.comparacarro.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val commonExtension = extensions.findByType(CommonExtension::class.java)
                ?: error("AndroidComposeConventionPlugin requires an Android plugin to be applied first.")

            configureAndroidCompose(commonExtension)
        }
    }
}
