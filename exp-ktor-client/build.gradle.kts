val ktorVersion: String by rootProject
val kotlinVersion: String by rootProject

plugins {
    application

    kotlin("jvm")
}

group = "com.siotman.experimental"
version = "0.1.0"

application {
    mainClass.set("com.siotman.experimental.ktor.client.App")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")

    // Ktor Engines
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    // Ktor Misc.
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}