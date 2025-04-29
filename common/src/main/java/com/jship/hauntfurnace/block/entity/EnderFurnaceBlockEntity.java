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
public class EnderFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public EnderFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(HauntFurnace.BlockEntities.ENDER_FURNACE.get(), blockPos, blockState, HauntFurnace.Recipes.CORRUPTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.ender_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new HauntFurnaceMenu(id, inventory, this, this.dataAccess);
    }
}
