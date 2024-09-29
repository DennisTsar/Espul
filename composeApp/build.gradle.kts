import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    jvm()

    js {
        browser {
            commonWebpackConfig {
                sourceMaps = false
            }
        }
        binaries.executable()
// https://youtrack.jetbrains.com/issue/KT-70904/K-JS-ES2015-causes-large-bundle-size-increase-with-Ktor-client
//        useEsModules()
//        compilerOptions.target = "es2015"
    }

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(libs.compose.lifecycle)
            // not included as it causes js build to be very slow
            // icons are instead manually copied: see components/icons
//            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.coil)
            implementation(libs.time.formatter)
            implementation(libs.material3.window.size)
            implementation(libs.multiplatform.settings)
            implementation(projects.markdownRenderer)
            implementation(projects.githubApi)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uitooling)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}

android {
    namespace = "io.github.opletter.espul"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "io.github.opletter.espul.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
//        resources.srcDirs("src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.opletter.espul.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}

// Bring sanity to the IJ gradle sync window
// Surely I should be able to do this with a gradle property
afterEvaluate {
    val resTaskNames = listOf(
        "convertXmlValueResourcesFor",
        "copyNonXmlValueResourcesFor",
        "prepareComposeResourcesTaskFor",
        "generateResourceAccessorsFor",
    )
    kotlin.sourceSets.forEach { sourceSet ->
        resTaskNames.forEach {
            gradle.startParameter.excludedTaskNames.add("$it${sourceSet.name}")
        }
    }
    kotlin.targets.filter { it !is KotlinMetadataTarget && it !is KotlinAndroidTarget }.forEach { target ->
        gradle.startParameter.excludedTaskNames.add("generateActualResourceCollectorsFor${target.name}")
    }
    gradle.startParameter.excludedTaskNames.add("generateComposeResClass")
    gradle.startParameter.excludedTaskNames.add("generateExpectResourceCollectorsForCommonMain")
    gradle.startParameter.excludedTaskNames.add("generateActualResourceCollectorsForAndroidMain")
}
