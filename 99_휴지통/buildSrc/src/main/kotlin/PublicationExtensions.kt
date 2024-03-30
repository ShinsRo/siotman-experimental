import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

/**
 * optional, gradle 프로젝트 프로퍼티; 릴리즈 배포 여부
 *
 * ex) ./gradlew publish -Ppublish.as_releases=[ true | false ] ...
 * true : version = ${project.version}
 * false : version = ${project.version}-SNAPSHOT
 */
private val Project.publishAsReleases: Boolean
    get() = properties["publish.as_releases"]?.toString()
        .equals("true", ignoreCase = true)

/**
 * optional, gradle 프로젝트 프로퍼티; 배포 대상 모듈.
 * 지정하지 않을 경우, 배포 설정을 하지 않습니다.
 *
 * ex) ./gradlew  publish -Ppublish.target_modules=utils-core,messages-kafka ...
 */
private val Project.publishTargetModules: List<String>
    get() = properties["publish.target_modules"]?.toString()
        ?.split(",") ?: emptyList()

private val Project.versionPostfix: String
    get() = if (publishAsReleases) "" else "-SNAPSHOT"

/**
 * 프로젝트(모듈)가 배포 대상인 경우, Github Pacakage 타겟으로 maven 배포를 설정합니다.
 */
fun Project.configurePublication() {
    if (name !in publishTargetModules) return

    println("$name - 패키지 배포 설정")
    println("배포 대상 모듈 : $name:$version$versionPostfix")

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    the(JavaPluginExtension::class).withSourcesJar()
    the(PublishingExtension::class).confiure(this)
}

private fun PublishingExtension.confiure(project: Project) {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            groupId = project.group.toString()
            version = "${project.version}${project.versionPostfix}"

            from(project.components["java"])
        }.also {
            println("Maven ${it.name} Repository 설정완료")
        }
    }
}
