package com.jship.hauntfurnace;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFabric;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryFabric;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import com.mojang.logging.LogUtils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class HauntFurnaceFabric implements ModInitializer {
        private static Block HAUNT_FURNACE_BLOCK;
        private static Item HAUNT_FURNACE_ITEM;
        private static BlockEntityType<HauntFurnaceBlockEntity> HAUNT_FURNACE_BLOCK_ENTITY;

        private static Block POWERED_HAUNT_FURNACE_BLOCK;
        private static Item POWERED_HAUNT_FURNACE_ITEM;
        private static BlockEntityType<PoweredHauntFurnaceBlockEntity> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

        private static RecipeType<HauntingRecipe> HAUNTING_RECIPE;
        private static RecipeSerializer<HauntingRecipe> HAUNTING_RECIPE_SERIALIZER;

        private static MenuType<HauntFurnaceMenu> HAUNT_FURNACE_MENU;
        private static MenuType<PoweredHauntFurnaceMenu> POWERED_HAUNT_FURNACE_MENU;

        private static EnergyStorageFactory<EnergyStorageWrapper> ENERGY_STORAGE_FACTORY;

        static {
                HAUNT_FURNACE_BLOCK = Registry.register(
                                Registry.BLOCK,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new HauntFurnaceBlock(FabricBlockSettings.copyOf(Blocks.FURNACE)));
                HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                Registry.BLOCK_ENTITY_TYPE,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunt_furnace"),
                                FabricBlockEntityTypeBuilder
                                                .create(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK)
                                                .build(null));
                HAUNT_FURNACE_ITEM = Registry.register(
                                Registry.ITEM,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new BlockItem(HAUNT_FURNACE_BLOCK,
                                                new FabricItemSettings().group(CreativeModeTab.TAB_DECORATIONS)));

                POWERED_HAUNT_FURNACE_BLOCK = Registry.register(
                                Registry.BLOCK,
                                new ResourceLocation(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new PoweredHauntFurnaceBlock(FabricBlockSettings.copyOf(Blocks.FURNACE)));
                POWERED_HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                Registry.BLOCK_ENTITY_TYPE,
                                new ResourceLocation(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                FabricBlockEntityTypeBuilder
                                                .create(PoweredHauntFurnaceBlockEntity::new,
                                                                POWERED_HAUNT_FURNACE_BLOCK)
                                                .build(null));
                POWERED_HAUNT_FURNACE_ITEM = Registry.register(
                                Registry.ITEM,
                                new ResourceLocation(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new BlockItem(POWERED_HAUNT_FURNACE_BLOCK,
                                                new FabricItemSettings().group(CreativeModeTab.TAB_DECORATIONS)));
                ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryFabric();
                EnergyStorage.SIDED.registerForBlockEntity(
                                (blockEntity, direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                                POWERED_HAUNT_FURNACE_BLOCK_ENTITY);

                HAUNTING_RECIPE = Registry.register(
                                Registry.RECIPE_TYPE,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunting"),
                                new RecipeType<HauntingRecipe>() {
                                        @Override
                                        public String toString() {
                                                return "hauntfurnace:haunting";
                                        }
                                });
                HAUNTING_RECIPE_SERIALIZER = Registry.register(
                                Registry.RECIPE_SERIALIZER,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunting"),
                                new SimpleCookingSerializer<>(HauntingRecipe::new, 200));
                HAUNT_FURNACE_MENU = Registry.register(
                                Registry.MENU,
                                new ResourceLocation(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new));
                POWERED_HAUNT_FURNACE_MENU = Registry.register(
                                Registry.MENU,
                                new ResourceLocation(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new));
        }

        @Override
        public void onInitialize() {
                HauntFurnace.HAUNT_FURNACE_BLOCK = () -> HAUNT_FURNACE_BLOCK;
                HauntFurnace.HAUNT_FURNACE_ITEM = () -> HAUNT_FURNACE_ITEM;
                HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY = () -> HAUNT_FURNACE_BLOCK_ENTITY;

                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK = () -> POWERED_HAUNT_FURNACE_BLOCK;
                HauntFurnace.POWERED_HAUNT_FURNACE_ITEM = () -> POWERED_HAUNT_FURNACE_ITEM;
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY = () -> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

                HauntFurnace.HAUNTING_RECIPE = () -> HAUNTING_RECIPE;
                HauntFurnace.HAUNTING_RECIPE_SERIALIZER = () -> HAUNTING_RECIPE_SERIALIZER;

                HauntFurnace.HAUNT_FURNACE_MENU = () -> HAUNT_FURNACE_MENU;
                HauntFurnace.POWERED_HAUNT_FURNACE_MENU = () -> POWERED_HAUNT_FURNACE_MENU;

                HauntFurnace.ENERGY_STORAGE_FACTORY = () -> ENERGY_STORAGE_FACTORY;
        }
}
