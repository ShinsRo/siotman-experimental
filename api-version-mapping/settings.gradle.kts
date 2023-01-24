rootProject.name = "api-version-mapping"


pluginManagement {
    val kotlin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version
    }
}
