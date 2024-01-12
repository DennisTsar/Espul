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
            api("org.jetbrains:markdown:0.6.1")
            // TODO: maybe compileOnly
            implementation(compose.runtime)
            implementation(compose.material3)
        }
    }
}
