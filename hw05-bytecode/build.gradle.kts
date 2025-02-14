import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

val logback: String by rootProject.extra
val mockito: String by rootProject.extra
val assertj: String by rootProject.extra

dependencies {
    implementation("ch.qos.logback:logback-classic:${logback}")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-core:${mockito}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")
}



tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("gradleTestRunner")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.bytecode.LogRunner"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}