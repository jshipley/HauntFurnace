package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFabric;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import team.reborn.energy.api.EnergyStorage;

public class HauntFurnaceFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        HauntFurnace.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            content.addAfter(Items.BLAST_FURNACE, HauntFurnace.Blocks.HAUNT_FURNACE.get(), HauntFurnace.Blocks.POWERED_HAUNT_FURNACE.get());
        });

        HauntFurnace.ENERGY_STORAGE_FACTORY = () -> new EnergyStorageFactoryFabric();
        EnergyStorage.SIDED.registerForBlockEntity(
            (blockEntity, direction) ->
                ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
            HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get()
        );
    }
}
