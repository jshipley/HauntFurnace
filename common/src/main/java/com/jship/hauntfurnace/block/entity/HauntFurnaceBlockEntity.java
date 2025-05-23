package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;

// Extends AbstractFurnaceBlockEntity adding the haunting block entity, recipe, name, and menu
public class HauntFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public HauntFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.HAUNT_FURNACE.get(), blockPos, blockState, ModRecipes.HAUNTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.haunt_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new HauntFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    @ExpectPlatform
    public static int getCustomBurnDuration(FuelValues fuelValues, ItemStack stack) {
        throw new AssertionError();
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
        return getCustomBurnDuration(fuelValues, stack);
    }

    @ExpectPlatform
    public static boolean isFuel(FuelValues fuelValues, ItemStack stack) {
        throw new AssertionError();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {       
        if (slot == HauntFurnaceBlockEntity.SLOT_RESULT) {
           return false;
        } else if (slot != HauntFurnaceBlockEntity.SLOT_FUEL) {
           return true;
        } else {
           return isFuel(this.level.fuelValues(), stack);
        }
     }
}
