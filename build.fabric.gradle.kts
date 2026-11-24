plugins {
    id("net.fabricmc.fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
}

stonecutter.properties.tags(sc.current.version)
version = "${property("mod.version")}+${sc.current.version}-fabric"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

loom {
    if (sc.current.version == "26.1") {
        accessWidenerPath.set(rootProject.file("src/main/resources/naturally_trimmed-26.1.accesswidener"))
    } else {
        accessWidenerPath.set(rootProject.file("src/main/resources/naturally_trimmed.accesswidener"))
    }

    mods {
        create("naturally_trimmed") {
            sourceSet(sourceSets["main"])
        }
    }

    runConfigs.all {
        this.generateRunConfig.set(stonecutter.current.isActive)
        this.runDirectory.set(file("../../run"))
    }
}

repositories {
    // Parchment mappings
    maven("https://maven.parchmentmc.org")

    // YACL
    maven("https://maven.isxander.dev/releases")

    // Mod Menu
    maven("https://maven.terraformersmc.com/")

    // Quilt Parser
    maven("https://maven.quiltmc.org/repository/release/")
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")

    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    // YACL
    implementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}-fabric")

    // Fabric API
    implementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    // Mod Menu
    implementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")

    // Quilt Parser
    implementation("org.quiltmc.parsers:json:${property("deps.quilt_parser")}")
    include("org.quiltmc.parsers:json:${property("deps.quilt_parser")}")
    implementation("org.quiltmc.parsers:gson:${property("deps.quilt_parser")}")
    include("org.quiltmc.parsers:gson:${property("deps.quilt_parser")}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    withSourcesJar()
}

tasks.processResources {
    // For some reason just property() isn't actually able to find the properties in this task,
    // so a shorthand for project.property() is used
    fun property(name: String): Any? = project.property(name)

    val props = buildMap {
        put("id", property("mod.id"))
        put("name", property("mod.name"))
        put("version", property("mod.version"))
        put("description", property("mod.description"))
        put("github_link", property("mod.github_link"))
        put("issues_link", property("mod.issues_link"))
        put("mc", property("mc.dep"))
        put("yacl", property("deps.yacl"))
        put("modmenu", property("deps.modmenu"))
        put("fabric_api", property("deps.fabric_api"))

        if (sc.current.version == "26.1") {
            put("aw_path", "naturally_trimmed-26.1.accesswidener")
        } else {
            put("aw_path", "naturally_trimmed.accesswidener")
        }
    }

    props.forEach(inputs::property)

    filesMatching("fabric.mod.json") { expand(props) }
    exclude(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml"))
}

tasks.named("publishMods") {
    onlyIf("PUBLISH_LOADER_CONSTRAINT excludes this configuration") {
        val loaderConstraint = providers.environmentVariable("PUBLISH_LOADER_CONSTRAINT")
        (!loaderConstraint.isPresent || loaderConstraint.get() == "fabric")
    }

    onlyIf("PUBLISH_MC_VERSION_CONSTRAINT excludes this configuration") {
        val mcVersionConstraint = providers.environmentVariable("PUBLISH_MC_VERSION_CONSTRAINT")
        (!mcVersionConstraint.isPresent || sc.eval(sc.current.version, mcVersionConstraint.get()))
    }
}

publishMods {
    displayName = "${property("mod.name")} ${property("mod.version")} for Fabric ${sc.current.version}"
    file.set(tasks.jar.get().archiveFile)
    version = property("mod.version").toString()
    changelog.set(
        rootProject.file("CHANGELOG.md")
            .takeIf { it.exists() }
            ?.readText()
            ?: "No changelog provided."
    )
    type = STABLE
    modLoaders.add("fabric")

    val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
    val curseforgeToken = providers.environmentVariable("CURSEFORGE_TOKEN")
    dryRun = !modrinthToken.isPresent || !curseforgeToken.isPresent

    val targets = property("mc.targets").toString().split(", ")

    modrinth {
        projectId.set("hHVaPgFK")
        accessToken = modrinthToken
        minecraftVersions.addAll(targets)
        optional("yacl")
        requires("fabric-api")
        optional("modmenu")
    }

    curseforge {
        projectId.set("1005441")
        accessToken = curseforgeToken
        minecraftVersions.addAll(targets)
        serverRequired = true
        optional("yacl")
        requires("fabric-api")
        optional("modmenu")
    }
}
