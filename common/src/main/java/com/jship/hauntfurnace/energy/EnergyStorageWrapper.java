package com.jship.hauntfurnace.energy;

public interface EnergyStorageWrapper {
    abstract boolean canExtract();
    abstract int extractEnergy(int maxExtract, boolean simulate);

    abstract boolean canReceive();
    abstract int receiveEnergy(int maxReceive, boolean simulate);
    
    abstract int getEnergyStored();
    abstract void setEnergyStored(int amount);

    abstract int getMaxEnergyStored();
}
