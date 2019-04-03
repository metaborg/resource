plugins {
  id("org.metaborg.gradle.config.root-project") version "0.3.7"
  id("org.metaborg.gitonium") version "0.1.1"
}

subprojects {
  metaborg {
    configureSubProject()
  }
}
