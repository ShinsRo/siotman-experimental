val kotlinVersion: String by rootProject

plugins {
    kotlin("jvm")
}

group = "com.siotman.experimental"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}