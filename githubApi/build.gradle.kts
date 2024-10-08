import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
    }
//    wasmWasi()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.ktor.main)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
