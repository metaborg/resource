plugins {
  `java-platform`
  `maven-publish`
}

val logVersion = "0.5.3"

val checkerframeworkVersion = "3.10.0"

val daggerVersion = "2.34.1"

dependencies {
  constraints {
    api("org.metaborg:log.dagger:$logVersion")

    api("org.checkerframework:checker-qual-android:$checkerframeworkVersion") // Use android version: annotation retention policy is class instead of runtime.

    api("com.google.dagger:dagger:$daggerVersion")
    api("com.google.dagger:dagger-compiler:$daggerVersion")
  }
}

publishing {
  publications {
    create<MavenPublication>("JavaPlatform") {
      from(components["javaPlatform"])
    }
  }
}
