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
