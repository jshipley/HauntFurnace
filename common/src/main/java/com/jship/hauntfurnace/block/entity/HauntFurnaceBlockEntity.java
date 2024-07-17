package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Extends AbstractFurnaceBlockEntity adding the haunting block entity, recipe, name, and menu
public class HauntFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public HauntFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY.get(), blockPos, blockState, HauntFurnace.HAUNTING_RECIPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.haunt_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new HauntFurnaceMenu(id, inventory, this, this.dataAccess);
    }
}
