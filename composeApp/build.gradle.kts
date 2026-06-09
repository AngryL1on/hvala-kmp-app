import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    android {
        namespace = "tech.appard.hvala.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        withJava()
        withHostTest {}
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.io.insert.koin.core)
            implementation(libs.io.insert.koin.compose)
            implementation(projects.shared.core.ui)
            implementation(projects.shared.core.i18n)
            implementation(projects.shared.core.datastore)
            implementation(projects.shared.core.database)
            implementation(projects.shared.core.dataNetwork)
            implementation(projects.shared.feature.auth.domain)
            implementation(projects.shared.feature.listings.data)
            implementation(projects.shared.feature.profile.data)
            implementation(projects.shared.feature.messages.data)
            implementation(projects.shared.feature.listings.domain)
            implementation(projects.shared.feature.profile.domain)
            implementation(projects.shared.feature.messages.domain)
            implementation(projects.shared.core.mvi)
            implementation(projects.shared.feature.auth.ui)
            implementation(projects.shared.feature.profile.ui)
            implementation(projects.shared.feature.settings.ui)
            implementation(projects.shared.feature.settings.domain)
            implementation(projects.shared.feature.settings.data)
            implementation(projects.shared.feature.listings.ui)
            implementation(projects.shared.feature.messages.ui)
            implementation(projects.shared.feature.favorites.ui)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
