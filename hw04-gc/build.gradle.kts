import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation ("com.google.guava:guava")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("testGc")
        archiveVersion.set("0.1")
    }

    build {
        dependsOn(shadowJar)
    }
}


val logback: String by rootProject.extra

dependencies {
    implementation("ch.qos.logback:logback-classic:${logback}")
}

