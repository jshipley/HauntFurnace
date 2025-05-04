package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.spiritapi.api.energy.SpiritEnergyStorage;
import com.jship.spiritapi.api.energy.fabric.SpiritEnergyStorageImpl;
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
            content.addAfter(Items.BLAST_FURNACE, HauntFurnace.Blocks.HAUNT_FURNACE.get(), HauntFurnace.Blocks.POWERED_HAUNT_FURNACE.get(), HauntFurnace.Blocks.ENDER_FURNACE.get(), HauntFurnace.Blocks.POWERED_ENDER_FURNACE.get());
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content -> {
            content.addAfter(Items.END_STONE, HauntFurnace.Blocks.GILDED_END_STONE.get());
        });

        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> ((SpiritEnergyStorageImpl) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage, HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get());
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> ((SpiritEnergyStorageImpl) ((PoweredEnderFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage, HauntFurnace.BlockEntities.POWERED_ENDER_FURNACE.get());
    }
}
