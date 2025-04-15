val assertj: String by rootProject.extra
val mockito: String by rootProject.extra
val reflections: String by rootProject.extra

dependencies {
    implementation("org.reflections:reflections:${reflections}")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:${assertj}")
    testImplementation("org.mockito:mockito-junit-jupiter:${mockito}")
}