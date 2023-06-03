plugins {
    `common-kotlin-conventions`
    `common-spring-conventions`
}

dependencies {
    val parentPath = parent?.path ?: ""
    implementation(project("$parentPath:domains"))
}