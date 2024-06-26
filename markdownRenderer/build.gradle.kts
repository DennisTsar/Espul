plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
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
