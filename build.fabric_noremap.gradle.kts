@file:Suppress("UnstableApiUsage")

plugins {
    id("net.fabricmc.fabric-loom")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
val edgeRelease = stonecutter.eval(mcVersion, ">26")
val supportsConnector = stonecutter.eval(mcVersion, "26.1")

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
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
    maven {
        name = "WTHIT"
        url = uri("https://maven2.bai.lol")
        content {
            includeGroupAndSubgroups("mcp.mobius.waila")
            includeGroupAndSubgroups("lol.bai")
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
        name = "Nucleoid Maven (Trinkets)"
        url = uri("https://maven.nucleoid.xyz")
        content {
            includeGroupAndSubgroups("eu.pb4")
            includeGroupAndSubgroups("xyz.nucleoid")
        }
    }
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        content {
            includeGroupAndSubgroups("fuzs")
        }
    }
    repositories {
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
    mavenCentral()
    mavenLocal()
}


dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")

    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    implementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    implementation("folk.sisby:surveyor:${property("deps.surveyor")}")

    if (edgeRelease) {
        implementation("garden.hestia:hoofprint:${property("deps.hoofprint")}")
        include("folk.sisby:surveyor:${property("deps.surveyor")}")
        include("garden.hestia:hoofprint:${property("deps.hoofprint")}")
    } else {
        implementation("maven.modrinth:hoofprint:${property("deps.hoofprint")}")
    }
    if (supportsConnector) {
        include("org.sinytra.forgified-fabric-api:fabric-api-base:2.0.3+b11575294c")
        include("org.sinytra:forgified-fabric-loader:2.5.75+0.18.4+26.1")
        include("org.sinytra.forgified-fabric-api:fabric-key-mapping-api-v1:2.0.4+05fccb0f4c")
        include("org.sinytra.forgified-fabric-api:fabric-networking-api-v1:6.3.1+64200f3a4c")
        include("org.sinytra.forgified-fabric-api:fabric-lifecycle-events-v1:4.1.1+150d8dbd4c")
        include("org.sinytra.forgified-fabric-api:fabric-command-api-v2:3.0.5+29e133704c")
        include("org.sinytra.forgified-fabric-api:fabric-rendering-v1:23.3.0+9e54f1904c")
        include("org.sinytra.forgified-fabric-api:fabric-events-interaction-v0:5.2.2+9abcb0834c")
    }

    implementation("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    include("folk.sisby:kaleido-config:${property("deps.kaleido")}")
    // Trinkets
    implementation("eu.pb4:trinkets:${property("deps.trinkets")}")
    // McQoy
    implementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    implementation("maven.modrinth:mcqoy:${property("deps.mcqoy")}")
    implementation("maven.modrinth:immersive-overlays:${property("deps.immersive_overlays")}")

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
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = JavaVersion.VERSION_25
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
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    type = STABLE
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("fabric")
    if (supportsConnector)
        modLoaders.add("neoforge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        if (supportsConnector)
            requires("launchpad")
        optional("mcqoy")
        optional("immersive-overlays")
        if (edgeRelease) {
            embeds("hoofprint")
            embeds("surveyor")
            environment = CLIENT_AND_SERVER
        } else {
            requires("hoofprint")
            requires("surveyor")
            environment = CLIENT_ONLY
        }
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        if (supportsConnector)
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
    }
}
