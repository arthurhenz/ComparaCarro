import extensions.implementation
import extensions.library
import extensions.libs
import extensions.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(plugin("ksp").pluginId)
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("koin-bom").get())) // tirar o add
            add("implementation", libs.findLibrary("koin-core").get())
            add("implementation", libs.findLibrary("koin-android").get())
            add("implementation", libs.findLibrary("koin-annotations").get())
            add("ksp", libs.findLibrary("koin-ksp-compiler").get())
        }
    }
}
