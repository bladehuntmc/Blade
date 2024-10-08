import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"

    `maven-publish`

    id("org.jetbrains.dokka") version "1.9.20"
}

allprojects {
    group = property("group") ?: "net.bladehunt"
    version = property("version") ?: "0.1.0"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://mvn.bladehunt.net/releases")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            compilerOptions { freeCompilerArgs.addAll("-Xinline-classes", "-Xcontext-receivers") }
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
}

dependencies {
    compileOnly(libs.minestom)
    compileOnly(libs.kotlinx.coroutines)

    api(libs.bundles.kotstom)
    api(libs.dotenv)

    // Optional dependencies
    compileOnly(libs.polar)

    testImplementation(libs.minestom)
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("blade")
    mergeServiceFiles()
    minimize()
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.build { dependsOn("shadowJar") }

java { withSourcesJar() }

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "blade"
        }
    }
    repositories {
        maven {
            name = "releases"
            url = uri("https://mvn.bladehunt.net/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
            authentication { create<BasicAuthentication>("basic") }
        }
    }
}
