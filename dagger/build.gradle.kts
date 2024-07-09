plugins {
    `java-library`
    `maven-publish`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
}

group = "org.metaborg"

dependencies {
    api(platform(project(":resource.depconstraints")))
    annotationProcessor(platform(project(":resource.depconstraints")))

    api(project(":resource"))
    api("org.metaborg:log.dagger")
    api("com.google.dagger:dagger")

    annotationProcessor("com.google.dagger:dagger-compiler")
    compileOnly("org.checkerframework:checker-qual-android")

mavenPublishConvention {
    repoOwner.set("metaborg")
    repoName.set("resource")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
