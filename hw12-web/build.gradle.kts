val flyway: String by project.extra
val hibernate: String by project.extra
val logback: String by project.extra
val postgresql: String by project.extra
val assertj: String by rootProject.extra
val lombok: String by rootProject.extra
val mockito: String by rootProject.extra
val h2: String by rootProject.extra


val jetty: String by project
val freemarker: String by project

dependencies {

    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jetty")
    implementation("org.eclipse.jetty:jetty-server:$jetty")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-webapp:$jetty")
    implementation("org.eclipse.jetty:jetty-security:$jetty")
    implementation("org.eclipse.jetty:jetty-http:$jetty")
    implementation("org.eclipse.jetty:jetty-io:$jetty")
    implementation("org.eclipse.jetty:jetty-util:$jetty")
    implementation("org.freemarker:freemarker:$freemarker")


    implementation ("org.projectlombok:lombok:${lombok}")
    annotationProcessor ("org.projectlombok:lombok:${lombok}")

    implementation("ch.qos.logback:logback-classic:${logback}")
    implementation("org.hibernate.orm:hibernate-core:${hibernate}")
    implementation("org.flywaydb:flyway-core:${flyway}")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:${flyway}")


    implementation("org.postgresql:postgresql:${postgresql}")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.h2database:h2:${h2}")
}