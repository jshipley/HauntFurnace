package com.jship.hauntfurnace.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageForge extends EnergyStorage implements EnergyStorageWrapper {

    EnergyStorageForge(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        super((int)capacity, (int)maxInsert, (int)maxExtract);
    }

    public void setEnergyStored(int amount) {
        this.energy = amount;
    }
}
