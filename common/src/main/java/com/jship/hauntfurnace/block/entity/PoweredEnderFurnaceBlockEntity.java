package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.PoweredEnderFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredEnderFurnaceBlockEntity extends AbstractPoweredFurnaceBlockEntity {

    public PoweredEnderFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.BlockEntities.POWERED_ENDER_FURNACE.get(), pos, state, HauntFurnace.Recipes.CORRUPTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.powered_ender_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PoweredEnderFurnaceMenu(id, inventory, this, this.poweredDataAccess);
    }
}
