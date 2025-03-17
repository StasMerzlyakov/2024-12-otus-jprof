val logback: String by rootProject.extra
val flyway: String by rootProject.extra
val postgresql: String by rootProject.extra

dependencies {
    implementation(project(":hw09-jdbc:api"))
    implementation(project(":hw09-jdbc:demo"))

    implementation("ch.qos.logback:logback-classic:${logback}")
    implementation("org.flywaydb:flyway-core:${flyway}")
    implementation("org.postgresql:postgresql:${postgresql}")
}
