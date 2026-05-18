@file:Suppress("UnstableApiUsage")

plugins {
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["mod_version"] = prop("mod.version") + "+" + prop("deps.minecraft")
        this["minecraft"] = prop("mod.mc_dep_fabric")
        this["mod_name"] = prop("mod.name")
        this["mod_description"] = prop("mod.description")
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }

}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
base.archivesName = property("mod.id") as String

//loom {
//    accessWidenerPath = rootProject.file("src/main/resources/${property("mod.id")}.accesswidener")
//}

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}

repositories {
    maven {
        name = "shedaniel (Cloth Config)"
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroupAndSubgroups("me.shedaniel")
        }
    }
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven {
        name = "Wisp Forest Maven"
        url = uri("https://maven.wispforest.io/releases/")
        content {
            includeGroupAndSubgroups("io.wispforest")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
    maven {
        name = "Sisby Maven"
        url = uri("https://repo.sleeping.town/")
        content {
            includeGroupAndSubgroups("folk.sisby")
        }
    }
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Xander Maven"
        url = uri("https://maven.isxander.dev/releases")
        content {
            includeGroupAndSubgroups("dev.isxander")
            includeGroupAndSubgroups("org.quiltmc.parsers")
        }
    }
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        content {
            includeGroupAndSubgroups("fuzs")
        }
    }
    maven {
        name = "FzzyMaven"
        url = uri("https://maven.fzzyhmstrs.me/")
        content {
            includeGroup("me.fzzyhmstrs")
        }
    }
    maven {
        name = "Cardinal Components"
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroupAndSubgroups("dev.onyxstudios")
            includeGroupAndSubgroups("org.ladysnake")
        }
    }
    maven {
        name = "Fabricators of Create (Snapshots)"
        url = uri("https://mvn.devos.one/snapshots")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
        }
    }
    maven {
        name = "Fabricators of Create (Releases)"
        url = uri("https://mvn.devos.one/releases")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://cursemaven.com")
            }
        }
        filter {
            includeGroup ("curse.maven")
        }
    }
    maven {
        name = "Gegy"
        url = uri("https://maven.gegy.dev/releases/")
        content {
            includeGroupAndSubgroups("dev.lambdaurora")
        }
    }
    maven {
        name = "Nucleoid Maven (Trinkets)"
        url = uri("https://maven.nucleoid.xyz")
        content {
            includeGroupAndSubgroups("eu.pb4")
            includeGroupAndSubgroups("xyz.nucleoid")
        }
    }
    mavenCentral()

}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
        mappings("dev.lambdaurora:yalmm-mojbackward:${property("deps.minecraft")}+build.${property("deps.mojbackward")}")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    modImplementation("maven.modrinth:surveyor:${property("deps.surveyor")}")
    modImplementation("maven.modrinth:hoofprint:${property("deps.hoofprint")}")
    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    include("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    // McQoy
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    modImplementation("maven.modrinth:mcqoy:${property("deps.mcqoy")}")
    // Trinkets
    if (stonecutter.eval(mcVersion, "<1.21.4")) {
        modCompileOnly("dev.emi:trinkets:${property("deps.trinkets")}") {
            exclude(group = "net.fabricmc")
        }
    } else {
        modCompileOnly("eu.pb4.fork:trinkets:${property("deps.trinkets")}") {
            exclude(group = "net.fabricmc")
        }
    }
    modImplementation("maven.modrinth:immersive-overlays:${property("deps.immersive_overlays")}")
    implementation("org.jspecify:jspecify:1.0.0")

    // Mixin Constraints - embedded
    implementation("com.moulberry:mixinconstraints:1.0.9")
    include("com.moulberry:mixinconstraints:1.0.9")

}

configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    }
}

stonecutter {
    replacements.string {
        direction = eval(current.version, ">26")
        replace("GuiGraphics", "GuiGraphicsExtractor")
    }
    replacements.string {
        direction = eval(current.version, ">26")
        replace("guiGraphics.drawString", "guiGraphics.text")
    }
    replacements.string {
        direction = eval(current.version, ">26")
        replace("guiGraphics.renderFakeItem", "guiGraphics.fakeItem")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.2")
        replace("pushPose", "pushMatrix")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.2")
        replace("popPose", "popMatrix")
    }
}

tasks {

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    type = STABLE
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("fabric")
    if (stonecutter.eval(mcVersion, "=1.21.1"))
        modLoaders.add("neoforge")
    if (stonecutter.eval(mcVersion, "=1.20.1"))
        modLoaders.add("forge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        if (stonecutter.eval(mcVersion, "=1.21.1") || stonecutter.eval(mcVersion, "=1.20.1"))
            requires("sinytra-connector")
        requires("hoofprint")
        requires("surveyor")
        optional("mcqoy")
        optional("immersive-overlays")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        if (stonecutter.eval(mcVersion, "=1.21.1") || stonecutter.eval(mcVersion, "=1.20.1"))
            requires("sinytra-connector")
        requires("hoofprint")
        requires("surveyor-map-framework")
        optional("mcqoy")
        optional("immersive-overlays")
    }
}
