@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.loom-back-compat")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val loader = sc.current.project.substringAfter('-')
val fabric = loader == "fabric"
val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
val supportsConnector = stonecutter.eval(mcVersion, "=1.21.1") || stonecutter.eval(mcVersion, "=1.20.1") || stonecutter.eval(mcVersion, "=26.1")
val supportsLaunchpad = stonecutter.eval(mcVersion, "=26.1")
val edgeRelease = stonecutter.eval(mcVersion, ">26.1.2") || !fabric
val unobfuscated = stonecutter.eval(mcVersion, ">26")

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

version = "${property("mod.version")}+${property("deps.minecraft")}-${loader}"
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
    maven {
        name = "Sinytra"
        url = uri("https://maven.sinytra.org")
        content {
            includeGroupAndSubgroups("org.sinytra")
        }
    }
    mavenLocal()
    mavenCentral()

}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    if (!unobfuscated) {
        mappings(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
            mappings("dev.lambdaurora:yalmm-mojbackward:${property("deps.minecraft")}+build.${property("deps.mojbackward")}")
        })
    }
    if (fabric) {
        modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
        modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
        modImplementation("maven.modrinth:mcqoy:${property("deps.mcqoy")}")
    } else {
        forgeUserdev("net.neoforged:neoforge:${property("deps.neoforge")}:userdev")
        implementation("org.sinytra.launchpad:launchpad:${property("deps.launchpad")}")
        implementation("org.sinytra:forgified-fabric-loader:${property("deps.fabric_loader")}")
        include("org.sinytra:forgified-fabric-loader:${property("deps.fabric_loader")}")

        implementation("org.sinytra.forgified-fabric-api:forgified-fabric-api:${property("deps.fabric_api")}")

        include("org.sinytra.forgified-fabric-api:fabric-api-base:${property("deps.fabric_base")}")
        include("org.sinytra.forgified-fabric-api:fabric-key-mapping-api-v1:${property("deps.fabric_key_mapping")}")
        include("org.sinytra.forgified-fabric-api:fabric-networking-api-v1:${property("deps.fabric_networking")}")
        include("org.sinytra.forgified-fabric-api:fabric-lifecycle-events-v1:${property("deps.fabric_lifecycle_events")}")
        include("org.sinytra.forgified-fabric-api:fabric-command-api-v2:${property("deps.fabric_commands")}")
        include("org.sinytra.forgified-fabric-api:fabric-rendering-v1:${property("deps.fabric_rendering")}")
        include("org.sinytra.forgified-fabric-api:fabric-events-interaction-v0:${property("deps.fabric_events_interaction")}")
    }

    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}") {
        exclude("net.fabricmc")
    }
    modImplementation("folk.sisby:surveyor:${property("deps.surveyor")}"){
        exclude("net.fabricmc")
    }
    modImplementation("cc.cassian.mru:mru-${loader}:${property("deps.mru")}+${property("deps.minecraft")}"){
        exclude("net.fabricmc")
    }

    if (unobfuscated) {
        implementation("garden.hestia:hoofprint:${property("deps.hoofprint")}"){
            exclude("net.fabricmc")
        }
        include("folk.sisby:surveyor:${property("deps.surveyor")}")
        include("garden.hestia:hoofprint:${property("deps.hoofprint")}")
        modCompileOnly("eu.pb4:trinkets:${property("deps.trinkets")}"){
            exclude(group = "net.fabricmc")
        }
    } else {
        modImplementation("maven.modrinth:hoofprint:${property("deps.hoofprint")}")
        modCompileOnly("dev.emi:trinkets:${property("deps.trinkets")}") {
            exclude(group = "net.fabricmc")
        }
    }
    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    include("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    // McQoy
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
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
        from(loomx.modJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=26")) {
        JavaVersion.VERSION_25
    } else if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
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
    file = loomx.modJar.map { it.archiveFile.get() }
    additionalFiles.from(loomx.modSourcesJar.map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    if (edgeRelease) {
        type = BETA
    } else {
        type = STABLE
    }
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-${loader}"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add(loader)
    if (supportsConnector && stonecutter.eval(mcVersion, "=1.21.1"))
        modLoaders.add("neoforge")
    if (supportsConnector && stonecutter.eval(mcVersion, "=1.20.1"))
        modLoaders.add("forge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        if (fabric)
            requires("fabric-api")
        if (supportsConnector)
            requires("connector")
        if (supportsLaunchpad)
            requires("launchpad")
        if (edgeRelease) {
            embeds("hoofprint")
            embeds("surveyor")
            environment = CLIENT_AND_SERVER
        } else {
            requires("hoofprint")
            requires("surveyor")
            environment = CLIENT_ONLY
        }
        requires("mru")
        optional("mcqoy")
        optional("immersive-overlays")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        if (fabric)
            requires("fabric-api")
        if (supportsConnector)
            requires("sinytra-connector")
        if (supportsLaunchpad)
            requires("launchpad")
        if (edgeRelease) {
            embeds("hoofprint")
            embeds("surveyor-map-framework")
            client = true
            server = true
        } else {
            requires("hoofprint")
            requires("surveyor-map-framework")
            client = true
            server = false
        }
        requires("mru")
        optional("mcqoy")
        optional("immersive-overlays")
    }
}
