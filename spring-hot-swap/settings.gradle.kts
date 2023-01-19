rootProject.name = "spring-hot-swap"

pluginManagement {
    val kotlin_version: String by settings
    val spring_boot_version: String by settings
    val spring_boot_dependency_management_version: String by settings

    plugins {
        id("org.springframework.boot") version spring_boot_version
        id("io.spring.dependency-management") version spring_boot_dependency_management_version

        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version
    }
}