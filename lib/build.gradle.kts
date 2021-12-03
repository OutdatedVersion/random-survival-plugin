import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.0"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

group = "com.outdatedversion"
version = "0.1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("com.google.guava:guava:30.1.1-jre")
    api("org.apache.commons:commons-math3:3.6.1")
}

tasks.processResources {
    expand(
        "main" to "com.outdatedversion.survival.Plugin",
        "version" to project.version
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.javaParameters = true
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("plugin-${project.version}-all.jar")
}
