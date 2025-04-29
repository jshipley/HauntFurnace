package com.jship.hauntfurnace.energy.neoforge;

import com.jship.hauntfurnace.energy.EnergyStorageWrapper;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.EnergyStorage;

public class EnergyStorageNeoforge extends EnergyStorage implements EnergyStorageWrapper {

    EnergyStorageNeoforge(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        super((int)capacity, (int)maxInsert, (int)maxExtract);
    }

    public void setEnergyStored(int amount) {
        this.energy = amount;
    }
}
