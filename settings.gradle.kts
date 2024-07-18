rootProject.name = "resource.root"

dependencyResolutionManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
        gradlePluginPortal()
    }
}

plugins {
    id("org.metaborg.convention.settings") version "latest.integration"
}

include(":resource.api")
project(":resource.api").name = "resource" // TODO: consider renaming "resource" to "resource.api"
include(":resource.dagger")
