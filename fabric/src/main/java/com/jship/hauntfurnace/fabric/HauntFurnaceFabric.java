package com.jship.hauntfurnace.fabric;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.data.fabric.FuelDataLoader;
import com.jship.hauntfurnace.data.fabric.FuelMap;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.energy.fabric.EnergyStorageFabric;
import com.jship.hauntfurnace.energy.fabric.EnergyStorageFactoryFabric;
import com.jship.hauntfurnace.network.fabric.Payloads;
import com.jship.hauntfurnace.network.fabric.Payloads.EnderFurnaceFuelMapS2CPayload;
import com.jship.hauntfurnace.network.fabric.Payloads.HauntFurnaceFuelMapS2CPayload;

import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import team.reborn.energy.api.EnergyStorage;

@Slf4j
public class HauntFurnaceFabric implements ModInitializer {
    private static EnergyStorageFactory<EnergyStorageWrapper> ENERGY_STORAGE_FACTORY;

    @Override
    public void onInitialize() {
        HauntFurnace.init();

        ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryFabric();

        ModBlockEntities.HAUNT_FURNACE.listen(e -> {
            ItemStorage.SIDED.registerForBlockEntity(
                    (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), e);
        });
        ModBlockEntities.POWERED_HAUNT_FURNACE.listen(e -> {
            ItemStorage.SIDED.registerForBlockEntity(
                    (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                    e);
            EnergyStorage.SIDED.registerForBlockEntity(
                    (blockEntity,
                            direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                    e);
        });
        ModBlockEntities.ENDER_FURNACE.listen(e -> {
            ItemStorage.SIDED.registerForBlockEntity(
                    (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                    e);
        });
        ModBlockEntities.POWERED_ENDER_FURNACE.listen(e -> {
            ItemStorage.SIDED.registerForBlockEntity(
                    (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), e);
            EnergyStorage.SIDED.registerForBlockEntity(
                    (blockEntity,
                            direction) -> ((EnergyStorageFabric) ((PoweredEnderFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                    e);
        });

        ModBlocks.GILDED_END_STONE.listen(b -> {
            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
                content.addAfter(Items.BLAST_FURNACE,
                        ModBlocks.HAUNT_FURNACE.value(), ModBlocks.POWERED_HAUNT_FURNACE.get(),
                        ModBlocks.ENDER_FURNACE.get(), ModBlocks.POWERED_ENDER_FURNACE.get());
            });

            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content -> {
                content.addAfter(Items.END_STONE, ModBlocks.GILDED_END_STONE.get());
            });
        });

        PayloadTypeRegistry.playS2C().register(Payloads.HauntFurnaceFuelMapS2CPayload.ID,
                Payloads.HauntFurnaceFuelMapS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(Payloads.EnderFurnaceFuelMapS2CPayload.ID,
                Payloads.EnderFurnaceFuelMapS2CPayload.CODEC);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FuelDataLoader());
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            log.debug("Sending fuel maps to player {}", player);
            if ((!FuelMap.HAUNT_FUEL_REFERENCE_MAP.isEmpty() && FuelMap.HAUNT_FUEL_MAP.isEmpty()) || !joined) {
                // this should happen when data is sent to player because resources were
                // reloaded. This may not be the best place to resolve the registry entries,
                // but it's the best one I've been able to find so far.
                FuelDataLoader.resolveFuelMapEntries(player.level().registryAccess(), "haunt_furnace_fuels",
                        FuelMap.HAUNT_FUEL_REFERENCE_MAP, FuelMap.HAUNT_FUEL_MAP);
                FuelDataLoader.resolveFuelMapEntries(player.level().registryAccess(), "ender_furnace_fuels",
                        FuelMap.ENDER_FUEL_REFERENCE_MAP, FuelMap.ENDER_FUEL_MAP);
            }
            ServerPlayNetworking.send(player, new HauntFurnaceFuelMapS2CPayload(FuelMap.HAUNT_FUEL_MAP));
            ServerPlayNetworking.send(player, new EnderFurnaceFuelMapS2CPayload(FuelMap.ENDER_FUEL_MAP));
        });

        HauntFurnace.ENERGY_STORAGE_FACTORY = () -> ENERGY_STORAGE_FACTORY;
    }
}
