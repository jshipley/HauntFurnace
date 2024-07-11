package com.jship.hauntfurnace.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface EnergyStorageFactory<T extends EnergyStorage> {
    public T createEnergyStorage(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity);
}
