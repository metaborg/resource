plugins {
    `java-library`
    `maven-publish`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
}

group = "org.metaborg"

dependencies {
    compileOnly(libs.checkerframework.android)

    testImplementation(libs.junit)
    testImplementation(libs.junit.params)
    testImplementation(libs.jimfs)
    testCompileOnly(libs.checkerframework.android)
}

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
