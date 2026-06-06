rootProject.name = "Hvala"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":composeApp")

// Shared core modules
include(":shared:core:ui")
include(":shared:core:contracts")
include(":shared:core:data-network")

// Feature modules inside shared
include(":shared:feature:ui-auth")
include(":shared:feature:ui-profile")
