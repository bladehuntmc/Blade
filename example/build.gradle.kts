plugins { kotlin("jvm") }

group = "net.bladehunt"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://mvn.bladehunt.net/releases")
}

dependencies {
    implementation(libs.minestom)

    implementation(project(":"))
    implementation(libs.kotlinx.coroutines)
    implementation(libs.minigamelib)

    implementation(libs.bundles.logback)

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }
