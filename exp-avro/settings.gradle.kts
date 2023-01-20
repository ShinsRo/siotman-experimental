rootProject.name = "exp-avro"

include("story-business")
include("story-data-rest")
include("story-schema-registry-server")
include("story-persistence")

pluginManagement {

    plugins {
        val kotlin_version: String by settings
        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version

        val spring_boot_version: String by settings
        val spring_boot_dependency_management_version: String by settings
        id("org.springframework.boot") version spring_boot_version
        id("io.spring.dependency-management") version spring_boot_dependency_management_version

    }

}