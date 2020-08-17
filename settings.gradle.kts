rootProject.name = "pong"

fun Settings.includeFrom(dir: String, name: String) {
    include(name)
    val moculeDir = "./$dir/${(name.removePrefix(":")).replace(":", "/")}"
    project(name).projectDir = File(moculeDir)
}

fun Settings.includeFromSrcDir(name: String) = includeFrom("src", name)

includeFromSrcDir(":lib")
includeFromSrcDir(":domain")
includeFromSrcDir(":server")
includeFromSrcDir(":client")

enableFeaturePreview("GRADLE_METADATA")
