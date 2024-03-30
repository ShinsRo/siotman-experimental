plugins {
    `common-kotlin-conventions`
    `common-spring-conventions`
}

dependencies {
    val parentPath = parent?.path ?: ""
    implementation(project("$parentPath:domains"))
    implementation(project("$parentPath:application"))
    implementation(project("$parentPath:infra"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
