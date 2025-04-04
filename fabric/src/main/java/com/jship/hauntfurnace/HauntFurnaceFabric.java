package com.jship.hauntfurnace;

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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
                                BuiltInRegistries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new HauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
                HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                BlockEntityType.Builder
                                                .of(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK)
                                                .build(null));
                ItemStorage.SIDED.registerForBlockEntity(
                                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), HAUNT_FURNACE_BLOCK_ENTITY);
                HAUNT_FURNACE_ITEM = Registry.register(
                                BuiltInRegistries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new BlockItem(HAUNT_FURNACE_BLOCK, new Item.Properties()));

                POWERED_HAUNT_FURNACE_BLOCK = Registry.register(
                                BuiltInRegistries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
                POWERED_HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                BlockEntityType.Builder
                                                .of(PoweredHauntFurnaceBlockEntity::new,
                                                                POWERED_HAUNT_FURNACE_BLOCK)
                                                .build(null));
                ItemStorage.SIDED.registerForBlockEntity(
                                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), POWERED_HAUNT_FURNACE_BLOCK_ENTITY);
                POWERED_HAUNT_FURNACE_ITEM = Registry.register(
                                BuiltInRegistries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new BlockItem(POWERED_HAUNT_FURNACE_BLOCK, new Item.Properties()));
                ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryFabric();
                EnergyStorage.SIDED.registerForBlockEntity(
                                (blockEntity, direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                                POWERED_HAUNT_FURNACE_BLOCK_ENTITY);

                HAUNTING_RECIPE = Registry.register(
                                BuiltInRegistries.RECIPE_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                                new RecipeType<HauntingRecipe>() {
                                        @Override
                                        public String toString() {
                                                return "hauntfurnace:haunting";
                                        }
                                });
                HAUNTING_RECIPE_SERIALIZER = Registry.register(
                                BuiltInRegistries.RECIPE_SERIALIZER,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                                new SimpleCookingSerializer<>(HauntingRecipe::new, 200));
                HAUNT_FURNACE_MENU = Registry.register(
                                BuiltInRegistries.MENU,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));
                POWERED_HAUNT_FURNACE_MENU = Registry.register(
                                BuiltInRegistries.MENU,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                                                FeatureFlags.VANILLA_SET));
        }

        @Override
        public void onInitialize() {
                ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
                        content.addAfter(Items.BLAST_FURNACE, HAUNT_FURNACE_BLOCK,
                                        POWERED_HAUNT_FURNACE_BLOCK);
                });

                // Store all of the registry references as suppliers so this works for NeoForge/Forge and Fabric
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
