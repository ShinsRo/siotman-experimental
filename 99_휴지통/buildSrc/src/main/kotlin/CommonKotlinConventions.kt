import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension

class CommonKotlinConventions : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.gradle.java")

        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
        apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
        apply(plugin = "org.jmailen.kotlinter")

        the(JavaPluginExtension::class).apply {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        tasks.withType(KotlinCompile::class.java) {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }

        tasks.withType(Test::class.java) {
            useJUnitPlatform()
        }

        dependencies {
            "implementation"(kotlin("stdlib-jdk8"))
            "implementation"(kotlin("reflect"))

            "testImplementation"(kotlin("test"))
            "testImplementation"(kotlin("test-junit5"))
        }
    }
}
