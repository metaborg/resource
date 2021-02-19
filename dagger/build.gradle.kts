plugins {
  id("org.metaborg.gradle.config.java-library")
}

dependencies {
  api(platform(project(":resource.depconstraints")))
  annotationProcessor(platform(project(":resource.depconstraints")))

  api(project(":resource"))
  api("org.metaborg:log.dagger")
  api("com.google.dagger:dagger")

  annotationProcessor("com.google.dagger:dagger-compiler")
  compileOnly("org.checkerframework:checker-qual-android")
}
