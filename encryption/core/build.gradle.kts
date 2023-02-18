plugins {
    kotlin("jvm")
}

group = "com.shinsro.encryption"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}