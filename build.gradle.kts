import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.firebaseAppdistribution) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    if (path != ":thirdparty:androidx:paging:compose") {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            debug = true
            ignoreFailures = false
            reporters {
                reporter(ReporterType.PLAIN)       // Console output
                reporter(ReporterType.CHECKSTYLE)  // XML for CI
                reporter(ReporterType.SARIF)       // SARIF for GitHub Code Scanning
            }
        }
    }
}
