package com.jship.hauntfurnace.datagen.fabric;

import java.util.concurrent.CompletableFuture;

import com.jship.hauntfurnace.HauntFurnace.ModBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    protected LootTableGenerator(FabricDataOutput exporter, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(exporter, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.HAUNT_FURNACE.get());
        dropSelf(ModBlocks.POWERED_HAUNT_FURNACE.get());
        dropSelf(ModBlocks.ENDER_FURNACE.get());
        dropSelf(ModBlocks.POWERED_ENDER_FURNACE.get());
        dropSelf(ModBlocks.GILDED_END_STONE.get());
    }
    
}
