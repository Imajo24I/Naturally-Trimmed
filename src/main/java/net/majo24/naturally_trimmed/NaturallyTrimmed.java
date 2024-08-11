package net.majo24.naturally_trimmed;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
//?} else if neoforge {
/*import net.majo24.naturally_trimmed.config.configscreen.screen.ConfigScreenProvider;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
/^? if >1.20.4 {^/
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else {
/^import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.fml.ModLoadingContext;
^///?}
*///?} else {
/*import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
*///?}

import net.majo24.naturally_trimmed.config.configscreen.screen.ConfigScreenProvider;
import net.majo24.naturally_trimmed.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/*? if forgeLike {*/ /*@Mod("naturally_trimmed") *///?}
public class NaturallyTrimmed /*? if fabric {*/ implements ModInitializer/*?}*/ {
    public static final String MOD_ID = "naturally_trimmed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ConfigClassHandler<Config> configHandler = Config.HANDLER;

    public static final LootItemFunctionType TRIM_RANDOMLY_FUNCTION = Registry.register(
            BuiltInRegistries.LOOT_FUNCTION_TYPE,
            ResourceLocation.tryBuild(MOD_ID, "trim_randomly_function"),
            new LootItemFunctionType(
                    //? if >1.20.1 {
                    TrimRandomlyFunction.CODEC
                     //?} else {
                    /*new TrimRandomlyFunction.Serializer()
                    *///?}
            ));


    public NaturallyTrimmed(/*? >1.20.4 && neoforge {*//*ModContainer container*//*?}*/) {
        onInitialize();

        // Register Config Screen
        //? if forgeLike {
        /*/^? <1.20.5 {^/
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ConfigScreenProvider.getConfigScreen(parent)));
        /^?} else {^/
        /^container.registerExtensionPoint(IConfigScreenFactory.class, getConfigScreen());
        ^//^?}^/*///?}
    }

    //? if fabric {
    @Override
            //?}
    public void onInitialize() {
        configHandler.load();
    }

    //? if neoforge && >1.20.4 {
    /*private IConfigScreenFactory getConfigScreen() {
        return (modContainer, screen) -> ConfigScreenProvider.getConfigScreen(screen);
    }
    *///?}

    public static Path getConfigPath() {
        //? if fabric {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        //?} else if forgeLike {
        /*Path configDir = FMLPaths.CONFIGDIR.get().resolve("naturally_trimmed.json5");
         *///?}
        return configDir.resolve("naturally_trimmed.json5");
    }
}