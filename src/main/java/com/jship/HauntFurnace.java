package com.jship;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemGroups;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HauntFurnace implements ModInitializer {
    public static final String MOD_ID = "hauntfurnace";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Block HAUNT_FURNACE_BLOCK;
    public static final Item HAUNT_FURNACE_ITEM;
    public static final BlockEntityType HAUNT_FURNACE_BLOCK_ENTITY;
    public static final RecipeType<HauntingRecipe> HAUNTING_RECIPE;
    public static final RecipeSerializer<HauntingRecipe> HAUNTING_RECIPE_SERIALIZER;
    public static final ScreenHandlerType<HauntFurnaceScreenHandler> HAUNT_FURNACE_SCREEN_HANDLER;
    public static final Identifier INTERACT_WITH_HAUNT_FURNACE = new Identifier(MOD_ID, "interact_with_haunt_furnace");

    static {
        HAUNT_FURNACE_BLOCK = Registry.register(
                Registries.BLOCK,
                new Identifier(MOD_ID, "haunt_furnace"),
                new HauntFurnaceBlock(FabricBlockSettings.copyOf(Blocks.FURNACE)));
        HAUNT_FURNACE_ITEM = Registry.register(
                Registries.ITEM,
                new Identifier(MOD_ID, "haunt_furnace"),
                new BlockItem(HAUNT_FURNACE_BLOCK, new FabricItemSettings()));
        HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "haunt_furnace"),
                FabricBlockEntityTypeBuilder.create(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK).build(null));
        HAUNTING_RECIPE = Registry.register(Registries.RECIPE_TYPE, new Identifier(MOD_ID, "haunting"), new RecipeType<HauntingRecipe>() {
            @Override
            public String toString() {return "hauntfurnace:haunting";}
        });
        HAUNTING_RECIPE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier(MOD_ID, "haunting"),
                new CookingRecipeSerializer<>(HauntingRecipe::new, 200));
        HAUNT_FURNACE_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                new Identifier(MOD_ID, "haunt_furnace"),
                new ScreenHandlerType(HauntFurnaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.BLAST_FURNACE, HAUNT_FURNACE_BLOCK);
        });

        Registry.register(Registries.CUSTOM_STAT, "interact_with_haunt_furnace", INTERACT_WITH_HAUNT_FURNACE);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_HAUNT_FURNACE, StatFormatter.DEFAULT);
    }
}
