kotlin {
    js {
        browser {
            distribution {
                directory = File("$buildDir/dist/")
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0-M1-1.4.0-rc")
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
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:1.3.2-1.4.0-rc")
                implementation("io.ktor:ktor-client-json-js:1.3.2-1.4.0-rc")
            }
        }
    }
}
