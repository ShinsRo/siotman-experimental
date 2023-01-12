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
    implementation("io.ktor:ktor-client-core:$ktorVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}