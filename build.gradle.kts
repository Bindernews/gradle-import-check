@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.bindernews.importcheck"
version = "0.1.0"


plugins {
    // So we can publish it
    id("com.gradle.plugin-publish") version "1.2.1"
    // I think we need this?
    id("java-gradle-plugin")
    // Plugin is written in Kotlin
    kotlin("jvm") version "1.6.21"
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")
    implementation("com.google.guava:guava:32.1.2-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    website.set("https://github.com/bindernews/gradle-import-check")
    vcsUrl.set(website.get())
    plugins {
        create("ImportCheckPlugin") {
            id = "net.bindernews.importcheck"
            implementationClass = "net.bindernews.importcheck.ImportCheckPlugin"
            displayName = "Import Check Plugin"
            description = "Compile-time class reference checking designed for modders"
            tags.set(listOf("compile", "java", "kotlin", "mod", "modding"))
        }
    }
}

// Minimum JVM version is Java 8
tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}
