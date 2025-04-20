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
        dropSelf(HauntFurnace.Blocks.HAUNT_FURNACE.get());
        // dropSelf(HauntFurnace.Blocks.POWERED_HAUNT_FURNACE.get());
    }
    
}
