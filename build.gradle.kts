buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
    id("java")
    kotlin("jvm") version "1.7.10"
}

dependencies {
    implementation(project("atum-api"))
    implementation(project("atum-craft"))
    implementation(project("atum-core"))
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        mavenLocal()

        // GitHub
        maven("https://jitpack.io")

        // mcMMO, BentoBox
        maven("https://repo.codemc.io/repository/maven-public/")

        // Spigot API, Bungee API
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

        //Minecraft repo
        maven("https://libraries.minecraft.net/")

        // PlaceholderAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

        // ProtocolLib
        maven("https://repo.dmulloy2.net/nexus/repository/public/")

        // WorldGuard
        maven("https://maven.enginehub.org/repo/")

        // MythicMobs
        maven("https://mvn.lumine.io/repository/maven-public/")

        // LibsDisguises
        maven("https://repo.md-5.net/content/groups/public/")

        maven("https://repo.techscode.com/repository/maven-releases/")
    }

    dependencies {

        //To not to shade the kotlin
        //compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")


        compileOnly("org.jetbrains:annotations:23.0.0")
        compileOnly("org.projectlombok:lombok:1.18.24")
        compileOnly("com.google.guava:guava:31.1-jre")

        annotationProcessor("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.jetbrains:annotations:23.0.0")

        // Test impl
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configurations.all {
        exclude(group = "org.slf4j", module = "slf4j-api")
    }

    configurations.testImplementation {
        setExtendsFrom(listOf(configurations.compileOnly.get(), configurations.implementation.get()))
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = "17"
            }
        }

        shadowJar {
            relocate("org.bstats", "me.phoenixra.atum.libs.bstats")
            relocate("org.checkerframework", "me.phoenixra.atum.libs.checkerframework")
            relocate("org.intellij", "me.phoenixra.atum.libs.intellij")
            relocate("org.jetbrains.annotations", "me.phoenixra.atum.libs.jetbrains.annotations")

            relocate("com.google.errorprone", "me.phoenixra.atum.libs.errorprone")
        }

        compileJava {
            dependsOn(clean)
            options.encoding = "UTF-8"
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            withSourcesJar()
        }

        test {
            useJUnitPlatform()

            // Show test results.
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        build {
            dependsOn(shadowJar)
        }
    }
}

group = "me.phoenixra"
version = findProperty("version")!!