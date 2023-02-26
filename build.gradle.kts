import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "2.3.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Rpm, TargetFormat.Deb)
            packageName = "Tic-Tac-Toe"
            packageVersion = "2.3.0"
            description = "A simple Tic-Tac-Toe game written in Compose for Desktop."
            copyright = "© 2023 Gabriel Brand. All rights reserved."
            licenseFile.set(project.file("LICENSE"))

//            linux {
//                iconFile.set(project.file("icon.png"))
//            }
        }
    }
}