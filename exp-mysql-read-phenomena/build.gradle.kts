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
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")

    implementation("mysql:mysql-connector-java:8.0.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}