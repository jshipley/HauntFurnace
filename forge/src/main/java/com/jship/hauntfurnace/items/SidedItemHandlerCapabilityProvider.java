package com.jship.hauntfurnace.items;

import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class SidedItemHandlerCapabilityProvider implements ICapabilityProvider {
   
    LazyOptional<? extends IItemHandler>[] sidedItemHandlers;

    public SidedItemHandlerCapabilityProvider(PoweredHauntFurnaceBlockEntity ent) {
        sidedItemHandlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(ent, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    }

    @Override
    @NotNull public <T> LazyOptional<T> getCapability(@NotNull final Capability<T> cap, final @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && side != null) {
            return switch(side) {
                case UP -> sidedItemHandlers[0].cast();
                case DOWN -> sidedItemHandlers[1].cast();
                case NORTH -> sidedItemHandlers[2].cast();
                case SOUTH -> sidedItemHandlers[3].cast();
                case EAST -> sidedItemHandlers[4].cast();
                default -> sidedItemHandlers[5].cast();
            };
        }
        return LazyOptional.of(null);
    }
    
}