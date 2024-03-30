import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

class CommonSpringConventions : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")

        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")

        tasks.withType(Jar::class) {
            enabled = true
        }

        tasks.withType(BootJar::class) {
            enabled = false
        }

        return@with
    }
}
