pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    val johnrengelman: String by settings
    val sonarlint: String by settings
    val spotless: String by settings
    val dependencyManagement: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelman
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
rootProject.name = "jprof"
include("hw01-gradle")
include("hw02-generics")
include("hw03-annotations")
include("hw04-gc")
