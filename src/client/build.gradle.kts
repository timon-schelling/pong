kotlin {
    js {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.6.12")
                implementation(project(":lib"))
                implementation(project(":domain"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

rootProject.extensions.findByType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().apply {
    this?.nodeVersion = "14.8.0"
}

(rootProject.tasks["kotlinNpmInstall"] as org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask).apply {}
