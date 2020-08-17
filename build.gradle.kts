plugins {
    idea
    kotlin("multiplatform") version "1.4.0-rc" apply false
    kotlin("plugin.serialization") version "1.4.0-rc" apply false
}

subprojects {
    version = rootProject.version
    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }
    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://mvnrepository.com/")
        maven("https://jitpack.io")
    }
}

task("distribute") {
    group = "distribute"
    dependsOn("build")
    dependsOn("copySubProjectDist")
}

task("copySubProjectDist", Copy::class) {
    group = "distribute"
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    subprojects.forEach {
        from("${it.buildDir}/dist")
    }
    into("$buildDir/dist")
}
