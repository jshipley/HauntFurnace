package com.jship.hauntfurnace.energy.fabric;

import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyStorageFactoryFabric implements EnergyStorageFactory<EnergyStorageWrapper> {

    public EnergyStorageFabric createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        return new EnergyStorageFabric(capacity, maxInsert, maxExtract, blockEntity);
    }
}
