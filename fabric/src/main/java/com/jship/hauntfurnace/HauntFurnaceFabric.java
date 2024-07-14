package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFabric;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryFabric;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class HauntFurnaceFabric implements ModInitializer {

        static {
                HauntFurnace.HAUNT_FURNACE_BLOCK = Registry.register(
                                BuiltInRegistries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new HauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
                HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                BlockEntityType.Builder
                                                .of(HauntFurnaceBlockEntity::new, HauntFurnace.HAUNT_FURNACE_BLOCK)
                                                .build(null));
                HauntFurnace.HAUNT_FURNACE_ITEM = Registry.register(
                                BuiltInRegistries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new BlockItem(HauntFurnace.HAUNT_FURNACE_BLOCK, new Item.Properties()));

                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK = Registry.register(
                                BuiltInRegistries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                BlockEntityType.Builder
                                                .of(PoweredHauntFurnaceBlockEntity::new,
                                                                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK)
                                                .build(null));
                HauntFurnace.POWERED_HAUNT_FURNACE_ITEM = Registry.register(
                                BuiltInRegistries.ITEM,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new BlockItem(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK, new Item.Properties()));
                HauntFurnace.ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryFabric();
                EnergyStorage.SIDED.registerForBlockEntity(
                                (blockEntity, direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY);

                HauntFurnace.HAUNTING_RECIPE = Registry.register(
                                BuiltInRegistries.RECIPE_TYPE,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                                new RecipeType<HauntingRecipe>() {
                                        @Override
                                        public String toString() {
                                                return "hauntfurnace:haunting";
                                        }
                                });
                HauntFurnace.HAUNTING_RECIPE_SERIALIZER = Registry.register(
                                BuiltInRegistries.RECIPE_SERIALIZER,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                                new SimpleCookingSerializer<>(HauntingRecipe::new, 200));
                HauntFurnace.HAUNT_FURNACE_MENU = Registry.register(
                                BuiltInRegistries.MENU,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                                new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));
                HauntFurnace.POWERED_HAUNT_FURNACE_MENU = Registry.register(
                                BuiltInRegistries.MENU,
                                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                                new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                                                FeatureFlags.VANILLA_SET));
        }

        @Override
        public void onInitialize() {
                ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
                        content.addAfter(Items.BLAST_FURNACE, HauntFurnace.HAUNT_FURNACE_BLOCK,
                                        HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK);
                });
        }
}
