package com.jship.hauntfurnace.datagen.fabric;

import java.util.concurrent.CompletableFuture;

import com.jship.hauntfurnace.HauntFurnace.ModBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class BlockTagGenerator extends FabricTagProvider<Block> {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.HAUNT_FURNACE.get())
            .add(ModBlocks.POWERED_HAUNT_FURNACE.get())
            .add(ModBlocks.ENDER_FURNACE.get())
            .add(ModBlocks.POWERED_ENDER_FURNACE.get())
            .add(ModBlocks.GILDED_END_STONE.get());
    }    
}
