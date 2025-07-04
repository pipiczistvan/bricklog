import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "hu.piware.barcode_scanner"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)

                api(libs.moko.permissions)
                api(libs.moko.permissions.compose)
                api(libs.moko.permissions.camera)

                implementation(libs.kermit)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.mlkit.barcode.scanning)
                implementation(libs.camera.mlkit.vision)

                implementation(libs.camera.camera2)
                implementation(libs.camera.view)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
