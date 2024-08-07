package net.majo24.naturally_trimmed.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.majo24.naturally_trimmed.NaturallyTrimmed;
import net.majo24.naturally_trimmed.config.custom_trim_combinations.CustomTrim;
import net.majo24.naturally_trimmed.config.custom_trim_combinations.TrimCombination;
import net.majo24.naturally_trimmed.config.subconfigs.TrimmedChestLoot;
import net.majo24.naturally_trimmed.config.subconfigs.TrimmedMobs;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.CustomTrimCombinations;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.General;
import net.majo24.naturally_trimmed.config.subconfigs.trimmedMobs.RandomTrims;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(ResourceLocation.tryBuild("naturally_trimmed", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(NaturallyTrimmed.getConfigPath())
                    .setJson5(true)
                    .build())
            .build();

    private final Map<List<String>, ArmorTrim> cachedCustomTrims = new HashMap<>();

    @SerialEntry
    public TrimmedMobs trimmedMobs = new TrimmedMobs();

    @SerialEntry
    public TrimmedChestLoot trimmedChestLoot = new TrimmedChestLoot();

    /**
     * @param requiredMaterial The material the trim combination has to match
     * @return A random trim combination that matches the given required material. Null if no trim combination matches.
     */
    @Nullable
    public TrimCombination getRandomTrimCombination(String requiredMaterial) {
        List<TrimCombination> trimCombinations = this.trimmedMobs.trimCombinations.trimCombinations;
        if (!trimCombinations.isEmpty()) {
            Collections.shuffle(trimCombinations);
            for (TrimCombination trimCombination : trimCombinations) {
                if (trimCombination.materialToApplyTo().equals(requiredMaterial)) {
                    return trimCombination;
                }
            }
        }
        return null;
    }


    public void addCustomTrimToCache(String material, String pattern, ArmorTrim trim) {
        this.cachedCustomTrims.put(Arrays.asList(material, pattern), trim);
    }

    @Nullable
    public ArmorTrim getOrCreateCachedTrim(String material, String pattern, RegistryAccess registryAccess) throws IllegalStateException {
        ArmorTrim cachedTrim = this.cachedCustomTrims.get(Arrays.asList(material, pattern));
        if (cachedTrim == null) {
            ArmorTrim newTrim = new CustomTrim(material, pattern).getTrim(registryAccess);
            this.addCustomTrimToCache(material, pattern, newTrim);
            return newTrim;
        }
        return cachedTrim;
    }
}
