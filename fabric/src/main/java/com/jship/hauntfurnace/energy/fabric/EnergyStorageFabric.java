package com.jship.hauntfurnace.energy.fabric;

import com.jship.hauntfurnace.energy.EnergyStorageWrapper;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class EnergyStorageFabric implements EnergyStorageWrapper {
    public final SimpleEnergyStorage fabricEnergyStorage;
    public final BlockEntity blockEntity;

    EnergyStorageFabric(long capacity, long maxInsert, long maxExtract, BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.fabricEnergyStorage = new SimpleEnergyStorage(capacity, maxInsert, maxExtract) {
            @Override
            protected void onFinalCommit() {
                blockEntity.setChanged();
            }
        };
    }

    public boolean canExtract() {
        return this.fabricEnergyStorage.supportsExtraction();
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        try (Transaction tx = Transaction.openOuter()) {
            int extracted = (int) this.fabricEnergyStorage.extract(maxExtract, tx);
            
            if (!simulate) {
                tx.commit();
            }
            return extracted;
        }
    }

    public boolean canReceive() {
        return this.fabricEnergyStorage.supportsInsertion();
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        try (Transaction tx = Transaction.openOuter()) {
            int inserted = (int) this.fabricEnergyStorage.insert(maxReceive, tx);
            if (!simulate) {
                tx.commit();
            }
            return inserted;
        }
    }

    public int getEnergyStored() {
        return (int) this.fabricEnergyStorage.getAmount();
    }

    public void setEnergyStored(int amount) {
        this.fabricEnergyStorage.amount = (long) amount;
    }

    public int getMaxEnergyStored() {
        return (int) this.fabricEnergyStorage.getCapacity();
    }
}
