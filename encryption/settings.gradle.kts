rootProject.name = "exp-rsa-and-eddsa"
include("core")

pluginManagement {
    val kotlin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
    }
}