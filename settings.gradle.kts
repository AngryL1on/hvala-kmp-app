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
include(":shared:core:mvi")
include(":shared:core:data-network")

// Feature modules
include(":shared:feature:auth:domain")
include(":shared:feature:auth:data")
include(":shared:feature:auth:ui")
include(":shared:feature:settings:ui")

include(":shared:feature:listings:domain")
include(":shared:feature:listings:data")
include(":shared:feature:listings:ui")

include(":shared:feature:profile:domain")
include(":shared:feature:profile:data")
include(":shared:feature:profile:ui")

include(":shared:feature:messages:domain")
include(":shared:feature:messages:data")
include(":shared:feature:messages:ui")

include(":shared:feature:favorites:ui")
