val logback: String by rootProject.extra
val mockito: String by rootProject.extra
val assertj: String by rootProject.extra
val postgresql: String by rootProject.extra
val flyway:  String by rootProject.extra
val hikari:  String by rootProject.extra

dependencies {
    implementation(project(":hw09-jdbc:api"))
    implementation("ch.qos.logback:logback-classic:${logback}")
    implementation("org.flywaydb:flyway-core:${flyway}")
    implementation("com.zaxxer:HikariCP:${hikari}")
    implementation("org.postgresql:postgresql:${postgresql}")

    runtimeOnly("org.flywaydb:flyway-database-postgresql:${flyway}")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

