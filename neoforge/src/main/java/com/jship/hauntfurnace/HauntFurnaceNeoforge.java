package com.jship.hauntfurnace;

import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.jship.hauntfurnace.data.neoforge.FuelData;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryNeoforge;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@Mod(HauntFurnace.MOD_ID)
public class HauntFurnaceNeoforge {
    public HauntFurnaceNeoforge(IEventBus modEventBus) {
        HauntFurnace.init();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::loadComplete);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerFuelMaps);

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> HauntFurnaceConfig.createConfig(parent));
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        HauntFurnace.ENERGY_STORAGE_FACTORY = EnergyStorageFactoryNeoforge::new;
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
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.HAUNT_FURNACE.get(),
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
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.ENDER_FURNACE.get(),
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

    private void registerFuelMaps(RegisterDataMapTypesEvent event) {
        event.register(FuelData.HAUNT_FUEL_MAP);
        event.register(FuelData.ENDER_FUEL_MAP);
    }
}
