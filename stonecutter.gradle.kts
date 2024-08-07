plugins {
    id("dev.kikugie.stonecutter") version "0.5-alpha.4"
    id("me.modmuss50.mod-publish-plugin") version "0.5.1"
}
stonecutter active "1.21-fabric" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "build"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "publishing"
    ofTask("publishMods")
}
