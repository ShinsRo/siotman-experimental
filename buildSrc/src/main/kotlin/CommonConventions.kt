@file:Suppress("ObjectPropertyName")

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

class CommonConventions : Plugin<Project> {
    override fun apply(target: Project) {}
}

inline val PluginDependenciesSpec.`common-conventions`: PluginDependencySpec
    get() = id("common-conventions")

inline val PluginDependenciesSpec.`common-kotlin-conventions`: PluginDependencySpec
    get() = id("common-kotlin-conventions")

inline val PluginDependenciesSpec.`common-spring-conventions`: PluginDependencySpec
    get() = id("common-spring-conventions")
