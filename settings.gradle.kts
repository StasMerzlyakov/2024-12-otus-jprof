pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    val johnrengelman: String by settings

    plugins {
        id("com.github.johnrengelman.shadow") version johnrengelman
    }
}
rootProject.name = "jprof"
include("L01-gradle")
