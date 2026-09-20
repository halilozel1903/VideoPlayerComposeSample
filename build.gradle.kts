plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

tasks.register("buildHealthCheck") {
    group = "verification"
    description = "Prints Gradle, Java, and module details for a quick CLI health check"
    doLast {
        println("Gradle ${gradle.gradleVersion}")
        println("Java ${System.getProperty("java.version")}")
        println("Root ${rootProject.name}")
        rootProject.subprojects.forEach { println("Module ${it.path}") }
    }
}

tasks.register("verifyDependencies") {
    group = "verification"
    description = "Resolves the app debug classpath so missing artifacts fail in CI"
    dependsOn(":app:checkDebugAarMetadata")
}

tasks.register("fullBuildVerification") {
    group = "verification"
    description = "Runs health check, dependency check, unit tests, and debug assemble"
    dependsOn(
        "buildHealthCheck",
        "verifyDependencies",
        ":app:testDebugUnitTest",
        ":app:assembleDebug"
    )
}
