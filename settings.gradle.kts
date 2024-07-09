rootProject.name = "resource.root"

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
}

plugins {
    id("org.metaborg.convention.settings") version "0.0.7"
}

include(":resource.api")
project(":resource.api").name = "resource" // TODO: consider renaming "resource" to "resource.api"
include(":resource.dagger")
