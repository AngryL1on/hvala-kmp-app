rootProject.name = "Hvala"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":composeApp")
include(":androidApp")

// Shared core modules
include(":shared:core:ui")
include(":shared:core:mvi")
include(":shared:core:i18n")
include(":shared:core:data-network")
include(":shared:core:datastore")
include(":shared:core:database")

// Feature modules
include(":shared:feature:auth:domain")
include(":shared:feature:auth:data")
include(":shared:feature:auth:ui")
include(":shared:feature:settings:domain")
include(":shared:feature:settings:data")
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
