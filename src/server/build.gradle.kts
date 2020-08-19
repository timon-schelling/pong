kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
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
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:1.3.2-1.4.0-rc")
                implementation("io.ktor:ktor-serialization:1.3.2-1.4.0-rc")
                implementation("io.ktor:ktor-websockets:1.3.2-1.4.0-rc")
                implementation("com.soywiz.korlibs.klock:klock-jvm:1.11.13")
            }
        }
    }
}

tasks["distribute"].dependsOn("copyJvmLib")

task("copyJvmLib", Copy::class) {
    dependsOn("build")
    group = "distribute"
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(kotlin.jvm().compilations["main"].compileDependencyFiles)
    from("$buildDir/libs")
    into("$buildDir/dist/lib")
}
