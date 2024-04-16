rootProject.name = "resource.root"

pluginManagement {
  repositories {
    maven("https://artifacts.metaborg.org/content/groups/public/")
  }
}


fun includeProject(path: String, id: String = "resource.${path.replace('/', '.')}") {
  include(id)
  project(":$id").projectDir = file(path)
}

includeProject("depconstraints")
include("resource")
project(":resource").projectDir = file("api") // TODO: consider renaming "resource" to "resource.api"
includeProject("dagger")
