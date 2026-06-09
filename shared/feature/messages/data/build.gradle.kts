import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    android {
        namespace = "tech.appard.hvala.shared.feature.messages.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    )

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.io.insert.koin.core)
            implementation(projects.shared.feature.messages.domain)
            implementation(projects.shared.feature.listings.domain)
            implementation(projects.shared.feature.profile.domain)
            implementation(projects.shared.feature.settings.domain)
            implementation(projects.shared.core.i18n)
            implementation(projects.shared.core.database)
        }
    }
}


compose.resources {
    publicResClass = true
    packageOfResClass = "tech.appard.hvala.shared.feature.messages.data.resources"
}
