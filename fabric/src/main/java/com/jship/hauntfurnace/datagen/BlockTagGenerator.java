package com.jship.hauntfurnace.datagen;

import java.util.concurrent.CompletableFuture;

import com.jship.hauntfurnace.HauntFurnace;

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
            .add(HauntFurnace.Blocks.HAUNT_FURNACE.get())
            .add(HauntFurnace.Blocks.POWERED_HAUNT_FURNACE.get())
            .add(HauntFurnace.Blocks.ENDER_FURNACE.get())
            .add(HauntFurnace.Blocks.POWERED_ENDER_FURNACE.get())
            .add(HauntFurnace.Blocks.GILDED_END_STONE.get());
    }    
}
