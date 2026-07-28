pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
        maven("https://maven.su5ed.dev/releases" ) {name = "Sinytra"}
        maven("https://repo.codemc.io/repository/relativitymc/") { name = "RelativityMC" }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
    id("dev.kikugie.loom-back-compat") version "0.4"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach {
                version("$version-$it", version).buildscript = "build.gradle.kts"
            }

        match("1.20.1", "fabric")
        match("1.21.1", "fabric")
        match("26.1", "fabric", "neoforge")
        match("26.2", "fabric")

        vcsVersion = "26.1-fabric"
    }
}