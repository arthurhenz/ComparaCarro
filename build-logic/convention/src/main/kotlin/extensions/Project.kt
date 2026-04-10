package extensions

/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency
import org.gradle.api.provider.Provider

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.version(dependency: String): Int = libs.findVersion(dependency).get().toString().toInt()

fun Project.plugin(pluginName: String): PluginDependency = libs.findPlugin(pluginName).get().get()

fun Project.library(dependency: String) = libs.findLibrary(dependency).get()

fun Project.platform(dependency: String): Provider<MinimalExternalModuleDependency> = project.dependencies.platform(library(dependency))

fun DependencyHandlerScope.implementation(dependency: Any) { add("implementation", dependency) }

fun DependencyHandlerScope.debugImplementation(dependency: Any) { add("debugImplementation", dependency) }

fun DependencyHandlerScope.androidTestImplementation(dependency: Any) { add("androidTestImplementation", dependency) }

fun DependencyHandlerScope.screenshotTestImplementation(dependency: Any) { add("screenshotTestImplementation", dependency) }
