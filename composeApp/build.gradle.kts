import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
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
            implementation(compose.materialIconsExtended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.io.insert.koin.core)
            implementation(libs.io.insert.koin.compose)
            implementation(projects.shared.core.ui)
            implementation(projects.shared.core.i18n)
            implementation(projects.shared.feature.auth.domain)
            implementation(projects.shared.core.dataNetwork)
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

android {
    namespace = "tech.appard.hvala"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "tech.appard.hvala"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

