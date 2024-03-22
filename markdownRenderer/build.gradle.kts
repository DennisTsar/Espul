plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    jvm()
    js { browser() }
//    macosX64()
//    macosArm64()
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            api(libs.markdown)
        }
    }
}

// TODO: Remove after updating compose
compose.kotlinCompilerPlugin = "1.5.11-kt-2.0.0-Beta5"
