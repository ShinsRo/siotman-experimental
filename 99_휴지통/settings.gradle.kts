rootProject.name = "siotman-experimental"

recursiveInclude("00_스터디")

// moduleDir 를 기준으로, *.gradle* 파일이 존재한다면, 대상 모듈 및 하위 모듈을 포함한다.
fun recursiveInclude(vararg moduleDirNames: String) = moduleDirNames.forEach { dirName ->
    val moduleDir = file(dirName)
    println(findSubProjectPaths(moduleDir))

    include(*(findSubProjectPaths(moduleDir).toTypedArray()))
}

// *.gradle* 파일명을 기준으로 프로젝트 패스를 검색한다.
fun findSubProjectPaths(projectDir: File, pathPrefix: String = ""): List<String> {
    if (projectDir.isDirectory.not()) return emptyList()
    val files = projectDir.listFiles() ?: return emptyList()

    val noScripts = files.map(File::getName)
        .find { ".gradle" in it } == null
    if (noScripts) return emptyList()

    val projectPath = "$pathPrefix${projectDir.name}"
    return listOf(projectPath) + files.map { findSubProjectPaths(it, "$projectPath:") }.flatten()
}
