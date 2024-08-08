package net.majo24.naturally_trimmed.config.configscreen.screen;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.majo24.naturally_trimmed.config.custom_trim_combinations.CustomTrim;
import net.majo24.naturally_trimmed.config.custom_trim_combinations.TrimCombination;
import net.majo24.naturally_trimmed.config.configscreen.Formatters;
import net.majo24.naturally_trimmed.config.configscreen.custom_trim_combinations.TrimCombinationsController;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.General;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static net.majo24.naturally_trimmed.NaturallyTrimmed.configHandler;

/*? <1.21 {*/
/*import net.minecraft.client.gui.screens.OptionsSubScreen;
 *//*?} else {*/
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*?}*/

import static net.minecraft.network.chat.Component.translatable;

public class ConfigScreen {
    private ConfigScreen() {}

    public static final Formatters.IntegerToPercentage integerToPercentageFormatter = new Formatters.IntegerToPercentage();
    public static final Formatters.TrimSystem trimSystemFormatter = new Formatters.TrimSystem();

    public static Screen getConfigScreen(Screen parent) {
        YetAnotherConfigLib.Builder configScreen = YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Naturally Trimmed"))
                .save(configHandler::save)

                .category(trimmedMobsCategory())
                .category(trimmedChestLootCategory());

        return configScreen.build().generateScreen(parent);
    }

    private static ConfigCategory trimmedMobsCategory() {
        return ConfigCategory.createBuilder()
                .name(translatable("naturally_trimmed.config.trimmed_mobs"))
                .tooltip(translatable("naturally_trimmed.config.trimmed_mobs.tooltip"))
                .group(trimmedMobsGeneralGroup())
                .group(trimmedMobsRandomTrimsGroup())
                .group(trimmedMobsTrimCombinationsGroup())
                .build();
    }

    private static OptionGroup trimmedMobsGeneralGroup() {
        return OptionGroup.createBuilder()
                .name(translatable("naturally_trimmed.config.trimmed_mobs.general"))
                .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.general.description")))
                .option(Option.<General.TrimSystems>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_mobs.general.trimSystem"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.general.trimSystem.description")))
                        .binding(configHandler.defaults().trimmedMobs.general.enabledSystem,
                                () -> configHandler.instance().trimmedMobs.general.enabledSystem,
                                enabledSystem -> configHandler.instance().trimmedMobs.general.enabledSystem = enabledSystem)
                        .controller(opt -> EnumControllerBuilder.create(opt)
                                .enumClass(General.TrimSystems.class)
                                .formatValue(trimSystemFormatter))
                        .build())

                .option(Option.<Integer>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_mobs.general.noTrimsChance"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.general.noTrimsChance.description")))
                        .binding(configHandler.defaults().trimmedMobs.general.noTrimsChance,
                                () -> configHandler.instance().trimmedMobs.general.noTrimsChance,
                                noTrimsChance -> configHandler.instance().trimmedMobs.general.noTrimsChance = noTrimsChance)
                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .formatValue(integerToPercentageFormatter))
                        .build())
                .build();
    }

    private static OptionGroup trimmedMobsRandomTrimsGroup() {
        return OptionGroup.createBuilder()
                .name(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims"))
                .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims.description")))

                .option(Option.<Integer>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims.trimChance"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims.trimChance.description")))
                        .binding(configHandler.defaults().trimmedMobs.randomTrims.trimChance,
                                () -> configHandler.instance().trimmedMobs.randomTrims.trimChance,
                                trimsChance -> configHandler.instance().trimmedMobs.randomTrims.trimChance = trimsChance)
                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .formatValue(integerToPercentageFormatter))
                        .build())

                .option(Option.<Integer>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims.similarTrimChance"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.randomTrims.similarTrimChance.description")))
                        .binding(configHandler.defaults().trimmedMobs.randomTrims.similarTrimChance,
                                () -> configHandler.instance().trimmedMobs.randomTrims.similarTrimChance,
                                similarTrimChance -> configHandler.instance().trimmedMobs.randomTrims.similarTrimChance = similarTrimChance)
                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .formatValue(integerToPercentageFormatter))
                        .build())

                .build();
    }

    private static OptionGroup trimmedMobsTrimCombinationsGroup() {
        return ListOption.<TrimCombination>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_mobs.trimCombinations"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_mobs.trimCombinations.description")))
                        .binding(configHandler.defaults().trimmedMobs.trimCombinations.trimCombinations,
                                () -> configHandler.instance().trimmedMobs.trimCombinations.trimCombinations,
                                trimCombinations -> configHandler.instance().trimmedMobs.trimCombinations.trimCombinations = trimCombinations)
                        .controller(TrimCombinationsController.Builder::create)
                        .initial(new TrimCombination("", CustomTrim.EMPTY, CustomTrim.EMPTY, CustomTrim.EMPTY, CustomTrim.EMPTY))
                        .build();
    }

    private static ConfigCategory trimmedChestLootCategory() {
        return ConfigCategory.createBuilder()
                .name(translatable("naturally_trimmed.config.trimmed_chest_loot"))
                .tooltip(translatable("naturally_trimmed.config.trimmed_chest_loot.description"))
                .option((Option.<Boolean>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_chest_loot.enabled"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_chest_loot.enabled.description")))
                        .binding(configHandler.defaults().trimmedChestLoot.enabled,
                                () -> configHandler.instance().trimmedChestLoot.enabled,
                                enabled -> configHandler.instance().trimmedChestLoot.enabled = enabled)
                        .controller(BooleanControllerBuilder::create)
                        .build()))
                .option(Option.<Integer>createBuilder()
                        .name(translatable("naturally_trimmed.config.trimmed_chest_loot.trimChance"))
                        .description(OptionDescription.of(translatable("naturally_trimmed.config.trimmed_chest_loot.trimChance.description")))
                        .binding(configHandler.defaults().trimmedChestLoot.trimChance,
                                () -> configHandler.instance().trimmedChestLoot.trimChance,
                                trimChance -> configHandler.instance().trimmedChestLoot.trimChance = trimChance)
                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .formatValue(integerToPercentageFormatter))
                        .build())
                .build();
    }

    public static class BackupScreen extends OptionsSubScreen {
        public BackupScreen(Screen parent) {
            super(parent, Minecraft.getInstance().options, Component.literal("Naturally Trimmed"));
        }

        @Override
        public void init() {
            MultiLineTextWidget messageWidget = new MultiLineTextWidget(
                    width / 2 - 110, height / 2 - 40,
                    translatable("naturally_trimmed.config.backup_screen.installYACL"),
                    minecraft.font);
            messageWidget.setMaxWidth(240);
            messageWidget.setCentered(true);
            addRenderableWidget(messageWidget);

            Button openLinkButton = Button.builder(translatable("naturally_trimmed.config.backup_screen.viewOnModrinth"),
                            button -> minecraft.setScreen(new ConfirmLinkScreen(
                                    open -> {
                                        if (open) Util.getPlatform().openUri("https://modrinth.com/mod/yacl");
                                        minecraft.setScreen(lastScreen);
                                    }, "https://modrinth.com/mod/yacl", true)))
                    .pos(width / 2 - 120, height / 2)
                    .size(115, 20)
                    .build();
            addRenderableWidget(openLinkButton);

            Button exitButton = Button.builder(CommonComponents.GUI_OK,
                            button -> onClose())
                    .pos(width / 2 + 5, height / 2)
                    .size(115, 20)
                    .build();
            addRenderableWidget(exitButton);
        }

        //? >=1.21 {
        @Override
        protected void addOptions() {
        }
        //?}

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            //? <=1.20.1
            /*renderDirtBackground(graphics);*/
            super.render(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, 5, 0xffffff);
        }
    }
}