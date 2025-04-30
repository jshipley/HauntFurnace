package com.jship.hauntfurnace.datagen;

import java.util.concurrent.CompletableFuture;

import com.jship.hauntfurnace.HauntFurnace;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    protected LootTableGenerator(FabricDataOutput exporter, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(exporter, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(HauntFurnace.HAUNT_FURNACE_BLOCK.get());
        dropSelf(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK.get());
        dropSelf(HauntFurnace.ENDER_FURNACE_BLOCK.get());
        dropSelf(HauntFurnace.POWERED_ENDER_FURNACE_BLOCK.get());
        dropSelf(HauntFurnace.GILDED_END_STONE_BLOCK.get());
    }
    
}
