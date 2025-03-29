import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

val logback: String by rootProject.extra
val flyway: String by rootProject.extra
val postgresql: String by rootProject.extra
val assertj: String by rootProject.extra

dependencies {
    implementation(project(":hw11-cache:api"))
    implementation(project(":hw11-cache:demo"))

    implementation("ch.qos.logback:logback-classic:${logback}")
    implementation("org.flywaydb:flyway-core:${flyway}")
    implementation("org.postgresql:postgresql:${postgresql}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("cache")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.HomeWork"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}