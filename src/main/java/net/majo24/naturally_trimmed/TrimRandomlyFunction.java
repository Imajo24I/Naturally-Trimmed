package net.majo24.naturally_trimmed;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.majo24.naturally_trimmed.NaturallyTrimmed.configHandler;

public class TrimRandomlyFunction extends LootItemConditionalFunction {
    //? if >1.20.1 {
    /*public static final
            //? if >1.20.4 {
    /^MapCodec<TrimRandomlyFunction> CODEC = RecordCodecBuilder.mapCodec(
     ^///?} else {
            /^Codec<TrimRandomlyFunction> CODEC = RecordCodecBuilder.create(
            ^///?}
            instance -> commonFields(instance)
                    .apply(instance, TrimRandomlyFunction::new)
    );
    *///?}

    protected TrimRandomlyFunction(
            //? if >1.20.1 {
            /*List<LootItemCondition>
             *///?} else {
            LootItemCondition[]
            //?}
                    predicates) {
        super(predicates);
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return NaturallyTrimmed.TRIM_RANDOMLY_FUNCTION;
    }

    /**
     * Called to perform the actual action of this function, after conditions have been checked.
     *
     * @param stack
     * @param context
     */
    @Override
    protected @NotNull ItemStack run(ItemStack stack, LootContext context) {
        RandomSource random = context.getRandom();

        if (!(stack.getItem() instanceof ArmorItem)
                || !configHandler.instance().trimmedChestLoot.enabled
                || (configHandler.instance().trimmedChestLoot.trimChance < random.nextInt(100))) {
            return stack;
        }

        RegistryAccess registryAccess = context.getLevel().registryAccess();

        ResourceKey<Registry<TrimMaterial>> materialKey = Registries.TRIM_MATERIAL;
        Registry<TrimMaterial> materialRegistry = registryAccess.registryOrThrow(materialKey);

        ResourceKey<Registry<TrimPattern>> patternKey = Registries.TRIM_PATTERN;
        Registry<TrimPattern> patternRegistry = registryAccess.registryOrThrow(patternKey);

        TrimApplier.applyRandomTrim(registryAccess, materialRegistry, patternRegistry, random, stack, null);
        return stack;
    }

    public static LootItemConditionalFunction.Builder<?> randomTrim() {
        return simpleBuilder(TrimRandomlyFunction::new);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<TrimRandomlyFunction.Builder> {
        @Override
        protected @NotNull Builder getThis() {
            return this;
        }

        @Override
        public @NotNull LootItemFunction build() {
            return new TrimRandomlyFunction(this.getConditions());
        }
    }

    //? if 1.20.1 {
    public static class Serializer extends LootItemConditionalFunction.Serializer<TrimRandomlyFunction> {
        @Override
        public TrimRandomlyFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new TrimRandomlyFunction(conditions);
        }
    }
    //?}
}
