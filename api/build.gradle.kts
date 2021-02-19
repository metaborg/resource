plugins {
  id("org.metaborg.gradle.config.java-library")
  id("org.metaborg.gradle.config.junit-testing")
}

dependencies {
  api(platform(project(":resource.depconstraints")))

  compileOnly("org.checkerframework:checker-qual-android")

  testCompileOnly("org.checkerframework:checker-qual-android")
}
