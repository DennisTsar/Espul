import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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
    wasmJs()
    wasmWasi()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)
        }
    }
}
