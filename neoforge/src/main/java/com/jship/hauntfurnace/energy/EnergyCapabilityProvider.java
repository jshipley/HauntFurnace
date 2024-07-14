package com.jship.hauntfurnace.energy;

import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
// import net.neoforged.neoforge.util.LazyOptional;

// public class EnergyCapabilityProvider implements ICapabilityProvider {
//     LazyOptional<IEnergyStorage> energyHandler;

//     public EnergyCapabilityProvider(PoweredHauntFurnaceBlockEntity ent) {
//         energyHandler = LazyOptional.of(() -> (EnergyStorage)ent.energyStorage);
//     }

//     @Override
//     @NotNull public <T> LazyOptional<T> getCapability(@NotNull final BlockCapability<T> cap, final @Nullable Direction side) {
//         return Capabilities.ENERGY.orEmpty(cap, energyHandler);
//     }
    
// }
