plugins {
  id("org.metaborg.gradle.config.root-project") version "0.3.15"
  id("org.metaborg.gradle.config.java-library") version "0.3.15"
  id("org.metaborg.gradle.config.junit-testing") version "0.3.15"
  id("org.metaborg.gitonium") version "0.1.2"
}

dependencies {
  compileOnly("org.checkerframework:checker-qual-android:3.0.0") // Use android version: annotation retention policy is class instead of runtime.
  testCompileOnly("org.checkerframework:checker-qual-android:3.0.0")
}
