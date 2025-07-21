import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.google.devtools.ksp.KspExperimental
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.io.FileInputStream
import java.util.Properties
import java.util.zip.ZipFile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.firebaseAppdistribution)
}

loadLocalProperties()

val archiveName = createArchiveName()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.kmpnotifier)
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.work.runtime)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(project.dependencies.platform(libs.android.firebase.bom))
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.csv)

            implementation(libs.bundles.ktor)

            implementation(libs.room.runtime)
            implementation(libs.room.paging)
            implementation(libs.sqlite.bundled)

            implementation(projects.thirdparty.androidx.paging.compose)
            implementation(projects.thirdparty.barcodeScanner)
            implementation(libs.qrose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.annotations)

            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.analytics)
            implementation(libs.gitlive.firebase.crashlytics)

            implementation(libs.bundles.coil)

            implementation(libs.korlibs.io)

            implementation(libs.kermit)

            implementation(libs.zoomable)

            implementation(libs.uri.kmp)

            api(libs.kmpnotifier)

            api(libs.moko.permissions)
            implementation(libs.moko.permissions.notifications)

            implementation(libs.kmpauth.google)
            implementation(libs.kmpauth.firebase)
            implementation(libs.kmpauth.uihelper)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }

    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin/org/koin/ksp/generated")
    }
}

android {
    namespace = "hu.piware.bricklog"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "hu.piware.bricklog"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.app.release.get().toInt()
        versionName = libs.versions.app.version.get()
        setProperty(
            "archivesBaseName",
            archiveName
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        if (rootProject.file("release/app-release.jks").exists()) {
            create("release") {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = properties["BRICKLOG_RELEASE_KEYSTORE_PWD"]?.toString()
                    ?: throw IllegalArgumentException("BRICKLOG_RELEASE_KEYSTORE_PWD not set")
                keyAlias = "bricklog"
                keyPassword = properties["BRICKLOG_RELEASE_KEY_PWD"]?.toString()
                    ?: throw IllegalArgumentException("BRICKLOG_RELEASE_KEY_PWD not set")
            }
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            versionNameSuffix = "-debug"
        }
        release {
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
            versionNameSuffix = "-release"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            ndk {
                debugSymbolLevel = "FULL"
            }
            firebaseAppDistribution {
                artifactType = "APK"
                groups = "testers"
                serviceCredentialsFile = "release/google-services-credentials.json"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

buildkonfig {
    packageName = "hu.piware.bricklog"

    val bricksetApiKey: String = properties["BRICKSET_API_KEY"]?.toString() ?: "<BRICKSET_API_KEY>"
    val googleAuthWebClientId: String =
        properties["GOOGLE_AUTH_WEB_CLIENT_ID"]?.toString() ?: "<GOOGLE_AUTH_WEB_CLIENT_ID>"

    defaultConfigs {
        buildConfigField(STRING, "FLAVOR", "PROD")
        buildConfigField(STRING, "BRICKSET_API_KEY", bricksetApiKey)
        buildConfigField(STRING, "GOOGLE_AUTH_WEB_CLIENT_ID", googleAuthWebClientId)
        buildConfigField(INT, "RELEASE_VERSION", libs.versions.app.release.get())
    }
    defaultConfigs("mock") {
        buildConfigField(STRING, "FLAVOR", "MOCK")
    }
    defaultConfigs("benchmark") {
        buildConfigField(STRING, "FLAVOR", "BENCHMARK")
    }
    defaultConfigs("dev") {
        buildConfigField(STRING, "FLAVOR", "DEV")
    }

    tasks.build.dependsOn("generateBuildKonfig")
}

baselineProfile {
    dexLayoutOptimization = true
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    ksp(libs.room.compiler)
    debugImplementation(compose.uiTooling)

    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosX64", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// Trigger Common Metadata Generation from Native tasks
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

ksp {
    arg("USE_COMPOSE_VIEWMODEL", "true")
    arg("KOIN_CONFIG_CHECK", "true")
    @OptIn(KspExperimental::class)
    useKsp2 = false // Had to disable to 'Trigger Common Metadata Generation from Native tasks' work
}

if (file("google-services.json").exists()) {
    apply(plugin = libs.plugins.google.services.get().pluginId)
    apply(plugin = libs.plugins.crashlytics.get().pluginId)
}

private fun loadLocalProperties() {
    val localProperties = Properties()
    val localPropertiesFile = rootDir.resolve("local.properties")

    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    localProperties.forEach { key, value ->
        if (!project.hasProperty(key.toString())) {
            extra.set(key.toString(), value)
        }
    }
}

private fun createArchiveName(): String {
    val versionCode = libs.versions.app.release.get().toInt()
    val versionName = libs.versions.app.version.get()
    val flavor = project.findProperty("buildkonfig.flavor") ?: "prod"
    return "bricklog-$versionName-$versionCode-$flavor"
}

val bundleReleaseApk by tasks.registering {
    group = "bundle"
    description = "Generate universal APK from the release AAB"

    val aab = layout.buildDirectory.file("outputs/bundle/release/$archiveName-release.aab")
    val apks = layout.buildDirectory.file("outputs/bundle/release/app-release.apks")
    val apkOutput = layout.buildDirectory.file("outputs/apk/release/$archiveName-release.apk")

    inputs.file(aab)
    outputs.file(apkOutput)

    dependsOn("bundleRelease")

    doLast {
        exec {
            commandLine(
                "java", "-jar", "../release/bundletool-all-1.18.1.jar",
                "build-apks",
                "--bundle=${aab.get().asFile}",
                "--output=${apks.get().asFile}",
                "--mode=universal",
                "--ks=../release/app-release.jks",
                "--ks-pass=pass:${properties["BRICKLOG_RELEASE_KEYSTORE_PWD"]}",
                "--ks-key-alias=bricklog",
                "--key-pass=pass:${properties["BRICKLOG_RELEASE_KEY_PWD"]}"
            )
        }
        // unzip the universal APK
        ZipFile(apks.get().asFile).use { zip ->
            val entry =
                zip.getEntry("universal.apk") ?: error("universal.apk not found in ${apks.get()}")
            zip.getInputStream(entry).use { input ->
                apkOutput.get().asFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    notCompatibleWithConfigurationCache("Gradle's configuration cache doesn't allow serializing script object references")
}
