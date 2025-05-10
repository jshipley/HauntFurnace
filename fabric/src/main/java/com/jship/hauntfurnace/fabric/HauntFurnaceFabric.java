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
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
                HauntFurnace.id("fuel_map_loader"), (provider) -> new FuelDataLoader(provider));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (!success)
                return;
            var hauntPayload = new HauntFurnaceFuelMapS2CPayload(FuelMap.HAUNT_FUEL_MAP);
            var enderPayload = new EnderFurnaceFuelMapS2CPayload(FuelMap.ENDER_FUEL_MAP);
            server.getPlayerList().getPlayers().forEach(player -> {
                ServerPlayNetworking.send(player, hauntPayload);
                ServerPlayNetworking.send(player, enderPayload);
            });
        });

        HauntFurnace.ENERGY_STORAGE_FACTORY = () -> ENERGY_STORAGE_FACTORY;
    }
}
