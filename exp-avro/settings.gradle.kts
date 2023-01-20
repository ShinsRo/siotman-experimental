rootProject.name = "exp-avro"

include("story-business")
include("story-data-client")
include("story-data-server")
include("story-persistence")
include("story-schema-registry-server")

pluginManagement {

    plugins {
        val kotlin_version: String by settings
        kotlin("jvm") version kotlin_version apply false
        kotlin("plugin.spring") version kotlin_version apply false

        val spring_boot_version: String by settings
        id("org.springframework.boot") version spring_boot_version apply false
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

}
