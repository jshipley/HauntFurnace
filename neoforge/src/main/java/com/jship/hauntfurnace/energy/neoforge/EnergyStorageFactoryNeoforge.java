package com.jship.hauntfurnace.energy.neoforge;

import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyStorageFactoryNeoforge implements EnergyStorageFactory<EnergyStorageWrapper> {

    public EnergyStorageWrapper createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        return new EnergyStorageNeoforge(capacity, maxInsert, maxExtract, blockEntity);
    }
}
