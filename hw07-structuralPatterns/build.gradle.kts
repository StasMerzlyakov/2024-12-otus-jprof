val logback: String by rootProject.extra
val mockito: String by rootProject.extra
val assertj: String by rootProject.extra

dependencies {
    implementation("ch.qos.logback:logback-classic:${logback}")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-core:${mockito}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")
}

