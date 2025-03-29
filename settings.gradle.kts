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
include("hw05-bytecode")
include("hw06-solid")
include("hw07-structuralPatterns")
include("hw08-json")
include("hw09-jdbc:demo")
findProject(":hw09-jdbc:demo")?.name = "demo"
include("hw09-jdbc:homework")
findProject(":hw09-jdbc:homework")?.name = "homework"
include("hw09-jdbc:api")
findProject(":hw09-jdbc:api")?.name = "api"
include("hw10-jpql")
