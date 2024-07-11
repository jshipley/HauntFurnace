package com.jship.hauntfurnace.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyStorageFactoryFabric implements EnergyStorageFactory<EnergyStorage> {
    public EnergyStorageFabric createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        return new EnergyStorageFabric(capacity, maxInsert, maxExtract, blockEntity);
    }
}
