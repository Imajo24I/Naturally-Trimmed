package net.majo24.naturally_trimmed;

import net.majo24.naturally_trimmed.trim_application.TrimLootTablesFunction;

//? if fabric {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else {
/*import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SequenceFunction;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//? if <26.3 {
/^import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import com.google.common.collect.ImmutableList;
^///?}
*///?}


public class Events {
    private Events() {}

    public static void registerEvents() {
        //? if fabric {
        addTrimFunctionToLootTables();
        //?} else
        //NeoForge.EVENT_BUS.addListener(Events::addTrimFunctionToLootTables);
    }

    //? if fabric {
    private static void addTrimFunctionToLootTables() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> builder.apply(TrimLootTablesFunction.builder().build()));
    }
    //?} else {
    /*private static void addTrimFunctionToLootTables(LootTableLoadEvent event) {
        LootTable table = event.getTable();

        //? if >=26.3 {
        if (table.modifier.isEmpty()) {
            // If an empty Optional, use the LootItemFunction directly
            table.modifier = Optional.of(Holder.direct(TrimLootTablesFunction.builder().build()));
        } else if (table.modifier.get().value() instanceof SequenceFunction sequence) {
            // If a SequenceFunction, add to the sequence
            List<Holder<LootItemFunction>> list = sequence.functions.stream().collect(Collectors.toList());
            list.add(Holder.direct(TrimLootTablesFunction.builder().build()));
            sequence.functions = HolderSet.direct(list);
        } else {
            // If a LootItemFunction, make it a SequenceFunction and add both functions to the sequence
            table.modifier = Optional.of(Holder.direct(SequenceFunction.of(new ArrayList<>(List.of(table.modifier.get(), Holder.direct(TrimLootTablesFunction.builder().build()))))));
        }
        //?} else {
        /^table.functions = ImmutableList.<LootItemFunction>builder()
                .addAll(table.functions)
                .add(TrimLootTablesFunction.builder().build())
                .build();

        table.compositeFunction = LootItemFunctions.compose(table.functions);
        ^///?}
    }
    *///?}
}
