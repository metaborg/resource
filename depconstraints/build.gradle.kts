plugins {
    `java-platform`
    `maven-publish`
}

val logVersion = "0.5.5"

val jakartaInjectVersion = "2.0.1"
val checkerframeworkVersion = "3.16.0"

val daggerVersion = "2.41"

dependencies {
    constraints {
        api("org.metaborg:log.dagger:$logVersion")

        api("jakarta.inject:jakarta.inject-api:$jakartaInjectVersion")
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
