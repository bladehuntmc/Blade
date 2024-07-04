plugins { kotlin("jvm") }

group = "net.bladehunt"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://mvn.bladehunt.net/releases")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:73b308673b")

    implementation(project(":"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

    implementation("ch.qos.logback:logback-core:1.5.6")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.fusesource.jansi:jansi:2.4.1")

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }
