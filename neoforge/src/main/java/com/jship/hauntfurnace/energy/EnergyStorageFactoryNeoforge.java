package com.jship.hauntfurnace.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyStorageFactoryNeoforge implements EnergyStorageFactory<EnergyStorageWrapper> {

    public EnergyStorageWrapper createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        return new EnergyStorageNeoforge(capacity, maxInsert, maxExtract, blockEntity);
    }
}
