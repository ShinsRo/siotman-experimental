import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
}

group = "com.siotman.experimental.exp-avro"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    val spring_boot_version: String by rootProject
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$spring_boot_version"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    val spring_cloud_version: String by rootProject
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:$spring_cloud_version"))
    implementation("org.springframework.cloud:spring-cloud-stream-schema-registry-server")

    val junit_version: String by rootProject
    implementation(platform("org.junit:junit-bom:$junit_version"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}