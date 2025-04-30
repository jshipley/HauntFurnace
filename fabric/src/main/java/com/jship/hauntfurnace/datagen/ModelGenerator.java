package com.jship.hauntfurnace.datagen;

import com.jship.hauntfurnace.HauntFurnace;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.TexturedModel;

public class ModelGenerator extends FabricModelProvider {

    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerators) {
        blockStateModelGenerators.createFurnace(HauntFurnace.HAUNT_FURNACE_BLOCK.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(HauntFurnace.ENDER_FURNACE_BLOCK.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createFurnace(HauntFurnace.POWERED_ENDER_FURNACE_BLOCK.get(), TexturedModel.ORIENTABLE);
        blockStateModelGenerators.createTrivialCube(HauntFurnace.GILDED_END_STONE_BLOCK.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
    }
    
}
