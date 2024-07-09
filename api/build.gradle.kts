plugins {
    `java-library`
    `maven-publish`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
}

group = "org.metaborg"
dependencies {
    api(platform(project(":resource.depconstraints")))

    compileOnly("org.checkerframework:checker-qual-android")
mavenPublishConvention {
    repoOwner.set("metaborg")
    repoName.set("resource")
}

    testImplementation("org.junit.jupiter:junit-jupiter-params:${metaborg.junitVersion}")
    testImplementation("com.google.jimfs:jimfs:1.1")
    testCompileOnly("org.checkerframework:checker-qual-android")
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
