import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.bindernews.importcheck"
version = "1.0.0-SNAPSHOT"


plugins {
    kotlin("jvm") version "1.6.21"
    id("java-gradle-plugin")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")
    implementation("com.google.guava:guava:32.1.2-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

gradlePlugin {
    plugins {
        create("ImportCheckPlugin") {
            id = "net.bindernews.importcheck"
            implementationClass = "net.bindernews.importcheck.ImportCheckPlugin"
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
