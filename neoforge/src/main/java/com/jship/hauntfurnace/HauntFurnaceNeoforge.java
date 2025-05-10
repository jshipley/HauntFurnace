package com.jship.hauntfurnace;

import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.neoforge.EnergyStorageFactoryNeoforge;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@Mod(HauntFurnace.MOD_ID)
public class HauntFurnaceNeoforge {

    public HauntFurnaceNeoforge(IEventBus modEventBus) {
        HauntFurnace.init();
        HauntFurnace.ENERGY_STORAGE_FACTORY = () -> new EnergyStorageFactoryNeoforge();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerSearchCategories);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.HAUNT_FURNACE.get());
            event.accept(ModBlocks.POWERED_HAUNT_FURNACE.get());
            event.accept(ModBlocks.ENDER_FURNACE.get());
            event.accept(ModBlocks.POWERED_ENDER_FURNACE.get());
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.GILDED_END_STONE.get());
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.POWERED_HAUNT_FURNACE.get(),
                (blockEntity, context) -> (EnergyStorage) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.HAUNT_FURNACE.get(),
                (blockEntity, side) -> {
                    return new SidedInvWrapper(blockEntity, side);
                });
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.POWERED_HAUNT_FURNACE.get(),
                (blockEntity, side) -> {
                    return new SidedInvWrapper(blockEntity, side);
                });
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.POWERED_ENDER_FURNACE.get(),
                (blockEntity, context) -> (EnergyStorage) ((PoweredEnderFurnaceBlockEntity) blockEntity).energyStorage);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.ENDER_FURNACE.get(),
                (blockEntity, side) -> {
                    return new SidedInvWrapper(blockEntity, side);
                });
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.POWERED_ENDER_FURNACE.get(),
                (blockEntity, side) -> {
                    return new SidedInvWrapper(blockEntity, side);
                });
    }

    private void registerSearchCategories(RegisterRecipeBookSearchCategoriesEvent event) {
        event.register(
                ModRecipes.HAUNTING_SEARCH_CATEGORY,
                ModRecipes.HAUNTING_BLOCKS_CATEGORY.get(),
                ModRecipes.HAUNTING_FOOD_CATEGORY.get(),
                ModRecipes.HAUNTING_MISC_CATEGORY.get());
        event.register(ModRecipes.CORRUPTING_SEARCH_CATEGORY,
                ModRecipes.CORRUPTING_BLOCKS_CATEGORY.get(),
                ModRecipes.CORRUPTING_FOOD_CATEGORY.get(),
                ModRecipes.CORRUPTING_MISC_CATEGORY.get());
    }
}
