plugins {
    kotlin("jvm")
}

group = "com.siotman.experimental.exp-avro"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    val junit_version: String by rootProject
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}