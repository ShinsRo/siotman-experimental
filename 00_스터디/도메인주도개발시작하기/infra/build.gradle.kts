plugins {
    `common-kotlin-conventions`
    `common-spring-conventions`
}

dependencies {
    val parentPath = parent?.path ?: ""
    implementation(project("$parentPath:domains"))
    implementation(project("$parentPath:application"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate:hibernate-core")
    runtimeOnly("com.h2database:h2")

    val querydslVersion = "5.0.0"
    implementation("com.querydsl:querydsl-jpa:$querydslVersion")
    annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion")

    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.6.2")
}
