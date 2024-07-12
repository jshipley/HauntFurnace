package com.jship.hauntfurnace.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyStorageFactoryForge implements EnergyStorageFactory<EnergyStorageWrapper> {
    public EnergyStorageWrapper createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        return new EnergyStorageForge(capacity, maxInsert, maxExtract, blockEntity);
    }
}
