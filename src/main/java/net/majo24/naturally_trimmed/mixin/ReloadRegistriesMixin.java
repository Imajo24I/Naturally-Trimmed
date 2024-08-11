package net.majo24.naturally_trimmed.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.majo24.naturally_trimmed.TrimRandomlyFunction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? if >1.20.4 {
import net.minecraft.server.ReloadableServerRegistries;
 //?} else {
/*import net.minecraft.world.level.storage.loot.LootDataManager;
*///?}

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(
//? if >1.20.4 {
        ReloadableServerRegistries.class
//?} else {
        /*LootDataManager.class
*///?}
)
public class ReloadRegistriesMixin {
    @WrapOperation(
            //? if >1.20.4 {
            method = "method_58278", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
            //?} else {
            /*//? if fabric {
            method = "method_51195", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
            //?} else {
            /^method = "method_51189", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
            ^///?}
            *///?}
    )
    private static <T> void modifyTables(Optional<T> optionalTable, Consumer<? super T> action, Operation<Void> original) {
        original.call(optionalTable.map(ReloadRegistriesMixin::addTrimRandomlyToTable), action);
    }

    @Unique
    private static <T> T addTrimRandomlyToTable(T value) {
        if (!(value instanceof LootTable table)) return value;

        if (table == LootTable.EMPTY) return value;

        //? if >1.20.1 {
        table.functions = ImmutableList.<LootItemFunction>builder()
                .addAll(table.functions)
                .add(TrimRandomlyFunction.randomTrim().build())
                .build();
        //?} else {
        /*table.functions = ArrayUtils.add(table.functions, TrimRandomlyFunction.randomTrim().build());
        *///?}

        return (T) table;
    }
}