val logback: String by rootProject.extra
val mockito: String by rootProject.extra
val assertj: String by rootProject.extra
val commonsLang3: String by rootProject.extra
dependencies {
    implementation("ch.qos.logback:logback-classic:${logback}")
    implementation("org.apache.commons:commons-lang3:${commonsLang3}")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-core:${mockito}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")
}
