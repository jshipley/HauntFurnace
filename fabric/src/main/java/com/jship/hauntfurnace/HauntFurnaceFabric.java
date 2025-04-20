package com.jship.hauntfurnace;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;


public class HauntFurnaceFabric implements ModInitializer {
        @Override
        public void onInitialize() {
                HauntFurnace.init();

                ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
                        content.addAfter(Items.BLAST_FURNACE, HauntFurnace.Blocks.HAUNT_FURNACE.get());
                                        // POWERED_HAUNT_FURNACE_BLOCK);
                });

                // HauntFurnace.ENERGY_STORAGE_FACTORY = () ->  new EnergyStorageFactoryFabric();
                // EnergyStorage.SIDED.registerForBlockEntity(
                //                 (blockEntity, direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                //                 HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE);
        }
}
