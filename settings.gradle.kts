rootProject.name = "resource.root"

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
    }
}


// This downloads an appropriate JVM if not already available
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


fun includeProject(path: String, id: String = "resource.${path.replace('/', '.')}") {
    include(id)
    project(":$id").projectDir = file(path)
}

includeProject("depconstraints")
include("resource")
project(":resource").projectDir = file("api") // TODO: consider renaming "resource" to "resource.api"
includeProject("dagger")
