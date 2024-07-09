rootProject.name = "resource.root"

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
}

plugins {
    id("org.metaborg.convention.settings") version "0.0.6"
}




fun includeProject(path: String, id: String = "resource.${path.replace('/', '.')}") {
    include(id)
    project(":$id").projectDir = file(path)
}

include("resource")
project(":resource").projectDir = file("api") // TODO: consider renaming "resource" to "resource.api"
includeProject("dagger")
