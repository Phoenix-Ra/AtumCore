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
    implementation(project("atum-core"))
    implementation(project("atum-plugin"))
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

        //Paper
        maven ("https://repo.papermc.io/repository/maven-public/")
        // GitHub
        maven("https://jitpack.io")

        // mcMMO, BentoBox
        maven("https://repo.codemc.io/repository/maven-public/")

        //Minecraft repo
        maven("https://libraries.minecraft.net/")

        // PlaceholderAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

        // ProtocolLib
        maven("https://repo.dmulloy2.net/nexus/repository/public/")

        //Crunch
        maven("https://redempt.dev")

        maven("https://repo.techscode.com/repository/maven-releases/")
    }

    dependencies {

        //To not to shade the kotlin
        //compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")

        //Paper
        compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
        
        implementation("org.reflections:reflections:0.10.2")

        compileOnly("com.google.guava:guava:31.1-jre")

        compileOnly("org.jetbrains:annotations:24.0.1")
        compileOnly("org.projectlombok:lombok:1.18.26")

        annotationProcessor("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.jetbrains:annotations:24.0.1")

        //caffeine
        implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")

        //PAPI
        compileOnly("me.clip:placeholderapi:2.11.2")

        // Test impl
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
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
                jvmTarget = "1.8"
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
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
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