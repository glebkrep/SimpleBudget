pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "simplebudget"
include(":app")
include(":core:data")
include(":feature:calculator")
include(":core:ui")
include(":feature:update-billing")
include(":feature:update-budget")
include(":feature:preferences")
include(":core:model")
include(":core:common")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:database")
include(":core:domain")
include(":core:testing")
include(":lint")
