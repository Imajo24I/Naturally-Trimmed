package net.majo24.naturally_trimmed;

import net.majo24.naturally_trimmed.config.custom_trim_combinations.CustomTrim;
import net.majo24.naturally_trimmed.config.custom_trim_combinations.TrimCombination;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.*;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.General.TrimSystems;

import static net.majo24.naturally_trimmed.NaturallyTrimmed.configHandler;

import java.util.Iterator;

//? >=1.20.5 {
/*import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
*///?}

public class TrimApplier {
    private TrimApplier() {
    }

    /**
     * Runs the enabled system on the armor
     *
     * @param registryAccess
     * @param random
     * @param armor          Armor to apply the trims on
     */
    public static void applyTrims(RegistryAccess registryAccess, RandomSource random, Iterable<ItemStack> armor) {
        if (configHandler.instance().trimmedMobs.general.noTrimsChance > random.nextInt(100)) {
            return;
        }

        TrimSystems enabledSystem = configHandler.instance().trimmedMobs.general.enabledSystem;

        if (enabledSystem == TrimSystems.RANDOM_TRIMS) {
            runRandomTrimsSystem(registryAccess, random, armor);
        } else if (enabledSystem == TrimSystems.CUSTOM_TRIM_COMBINATIONS) {
            runCustomTrimCombinationsSystem(armor, registryAccess);
        }
    }

    /**
     * Runs the random trims system on the given armor
     *
     * @param registryAccess
     * @param random
     * @param armor          Armor to apply the trims on
     */
    public static void runRandomTrimsSystem(RegistryAccess registryAccess, RandomSource random, Iterable<ItemStack> armor) {
        ResourceKey<Registry<TrimMaterial>> materialKey = Registries.TRIM_MATERIAL;
        Registry<TrimMaterial> materialRegistry = registryAccess.registryOrThrow(materialKey);
        ResourceKey<Registry<TrimPattern>> patternKey = Registries.TRIM_PATTERN;
        Registry<TrimPattern> patternRegistry = registryAccess.registryOrThrow(patternKey);

        ArmorTrim lastTrim = null;

        int trimChance = configHandler.instance().trimmedMobs.randomTrims.trimChance;

        for (ItemStack armorPiece : armor) {
            if (trimChance < random.nextInt(100)) {
                continue;
            }
            if (armorPiece.getItem() != Items.AIR) {
                lastTrim = applyRandomTrim(registryAccess, materialRegistry, patternRegistry, random, armorPiece, lastTrim);
            }
        }
    }

    /**
     * Runs the custom trim combinations system on the given armor
     *
     * @param armor          Armor to apply the trims on
     * @param registryAccess
     */
    public static void runCustomTrimCombinationsSystem(Iterable<ItemStack> armor, RegistryAccess registryAccess) {
        String requiredMaterial = "";

        for (ItemStack armorPiece : armor) {
            if (armorPiece.getItem() != Items.AIR) {
                requiredMaterial = getArmorMaterial(armorPiece);
                break;
            }
        }
        if (requiredMaterial.isEmpty()) {
            return;
        }

        TrimCombination trimCombination = configHandler.instance().getRandomTrimCombination(requiredMaterial);
        if (trimCombination == null) {
            return;
        }

        Iterator<ItemStack> armorIterable = armor.iterator();

        for (CustomTrim trim : trimCombination.trims()) {
            ItemStack armorPiece = armorIterable.next();
            if (armorPiece.getItem() == Items.AIR) {
                continue;
            }

            ArmorTrim armorTrim = configHandler.instance().getOrCreateCachedTrim(trim.material(), trim.pattern(), registryAccess);
            if (armorTrim == null) {
                continue;
            }

            applyTrim(armorPiece, armorTrim, registryAccess);
        }
    }

    /**
     * Get the material of the given armor piece
     */
    private static String getArmorMaterial(ItemStack armorPiece) {
        if (armorPiece.toString().contains("netherite")) {
            return "netherite";
        } else if (armorPiece.toString().contains("diamond")) {
            return "diamond";
        } else if (armorPiece.toString().contains("gold")) {
            return "gold";
        } else if (armorPiece.toString().contains("iron")) {
            return "iron";
        } else if (armorPiece.toString().contains("chain")) {
            return "chain";
        } else if (armorPiece.toString().contains("leather")) {
            return "leather";
        } else {
            NaturallyTrimmed.LOGGER.error("Could not find armor material for {}", armorPiece);
            return "";
        }
    }

    /**
     * Apply a trim on the armor
     *
     * @param armorPiece Armor to apply the trim on
     * @param armorTrim  Trim to apply on the armor
     */
    public static void applyTrim(ItemStack armorPiece, ArmorTrim armorTrim, RegistryAccess registryAccess) {
        //? >=1.20.5 {
        /*armorPiece.applyComponents(DataComponentPatch.builder().set(DataComponents.TRIM, armorTrim).build());
        *///?} else {
        ArmorTrim.setTrim(registryAccess, armorPiece, armorTrim);
         //?}
    }

    /**
     * Applies a random trim on the given armor piece. The random trim orients on trimToTakeIntoAccount
     *
     * @param armorPiece            Armor piece to apply the trim on
     * @param trimToTakeIntoAccount The trim, the new random trim should orient on
     * @return The random trim which was used
     */
    public static ArmorTrim applyRandomTrim(RegistryAccess registryAccess, Registry<TrimMaterial> materialRegistry, Registry<TrimPattern> patternRegistry, RandomSource random, ItemStack armorPiece, ArmorTrim trimToTakeIntoAccount) {
        Holder.Reference<TrimMaterial> randomTrimMaterial = materialRegistry.getRandom(random).orElseThrow();
        Holder.Reference<TrimPattern> randomTrimPattern = patternRegistry.getRandom(random).orElseThrow();
        ArmorTrim armorTrim = new ArmorTrim(randomTrimMaterial, randomTrimPattern);

        if (trimToTakeIntoAccount != null) {
            int similarTrimChance = configHandler.instance().trimmedMobs.randomTrims.similarTrimChance;

            if (similarTrimChance >= random.nextInt(100)) {
                armorTrim = new ArmorTrim(trimToTakeIntoAccount.material(), armorTrim.pattern());
            }

            if (similarTrimChance >= random.nextInt(100)) {
                armorTrim = new ArmorTrim(armorTrim.material(), trimToTakeIntoAccount.pattern());
            }
        }

        applyTrim(armorPiece, armorTrim, registryAccess);
        return armorTrim;
    }
}