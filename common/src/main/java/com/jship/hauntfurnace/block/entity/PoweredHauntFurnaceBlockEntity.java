package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredHauntFurnaceBlockEntity extends AbstractPoweredFurnaceBlockEntity {

    public PoweredHauntFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get(), pos, state, HauntFurnace.Recipes.HAUNTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.powered_haunt_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PoweredHauntFurnaceMenu(id, inventory, this, this.poweredDataAccess);
    }
}
