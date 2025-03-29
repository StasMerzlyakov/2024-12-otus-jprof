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
