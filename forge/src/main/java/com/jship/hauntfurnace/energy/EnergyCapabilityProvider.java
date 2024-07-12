package com.jship.hauntfurnace.energy;

import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCapabilityProvider implements ICapabilityProvider {
    LazyOptional<IEnergyStorage> energyHandler;

    public EnergyCapabilityProvider(PoweredHauntFurnaceBlockEntity ent) {
        energyHandler = LazyOptional.of(() -> (EnergyStorage)ent.energyStorage);
    }

    @Override
    @NotNull public <T> LazyOptional<T> getCapability(@NotNull final Capability<T> cap, final @Nullable Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, energyHandler);
    }
    
}
