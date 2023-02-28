import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val SERENITY = "3.6.12"
    const val APPIUM = "8.3.0"
}

object Project {
    const val GROUP_ID = "com.bookverse.automation"
    const val ARTIFACT_ID = "bookverse-automation-common"
}

plugins {
    id("java-library")
    id("idea")
    id("maven-publish")
    id("net.serenity-bdd.serenity-gradle-plugin") version "3.6.7"
    id("pl.allegro.tech.build.axion-release") version "1.13.2"
    kotlin("jvm") version "1.8.0"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    api("io.appium:java-client:${Versions.APPIUM}")
    api("net.serenity-bdd:serenity-screenplay:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-core:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-junit:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-cucumber:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-rest-assured:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-ensure:${Versions.SERENITY}")
    api("net.serenity-bdd:serenity-screenplay-rest:${Versions.SERENITY}")
    api("org.apache.kafka:kafka-clients:3.3.1")
    api("org.postgresql:postgresql:42.5.1")
    api("redis.clients:jedis:4.3.1")
    api("org.mongodb:mongodb-driver-sync:4.8.2")
    api("com.googlecode.json-simple:json-simple:1.1.1")

    testImplementation(kotlin("test"))
    testImplementation("org.slf4j:slf4j-simple:2.0.6")
}

serenity {
    testRoot = "bookverse-automation-common"
    requirementsBaseDir = "src/test/resources/features"
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    finalizedBy("aggregate")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Project.GROUP_ID
            artifactId = Project.ARTIFACT_ID
            version = scmVersion.version
            from(components["java"])
        }
    }

    repositories {

        maven {
            url = uri("https://repo1.maven.org/maven2/")
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "6f2db1ed-656c-4eb7-83e1-5ef75de5a3b2"
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }

        mavenLocal()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.jar {
    enabled = true
}

scmVersion {
    repository.pushTagsOnly = true
    repository.customUsername = "Admin"
    repository.customPassword = "Admin123"
}