package com.jship.hauntfurnace.datagen;

import com.jship.hauntfurnace.HauntFurnace.ModBlocks;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.TexturedModel;

public class ModelGenerator extends FabricModelProvider {

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerators) {
        blockStateModelGenerators.createFurnace(ModBlocks.HAUNT_FURNACE.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(ModBlocks.POWERED_HAUNT_FURNACE.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(ModBlocks.ENDER_FURNACE.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(ModBlocks.POWERED_ENDER_FURNACE.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createTrivialCube(ModBlocks.GILDED_END_STONE.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
    }
    
}
