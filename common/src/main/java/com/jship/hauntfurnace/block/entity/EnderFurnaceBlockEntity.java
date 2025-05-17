package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Extends AbstractFurnaceBlockEntity adding the haunting block entity, recipe, name, and menu
public class EnderFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public EnderFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ENDER_FURNACE.get(), blockPos, blockState, ModRecipes.CORRUPTING.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.ender_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new EnderFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    @ExpectPlatform
    public static boolean isFuel(ItemStack stack) {
        throw new AssertionError();
    }

    @Override
    // override so that this class's isFuel will be called instead of the abstract class's isFuel
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        } else if (slot != 1) {
            return true;
        } else {
            ItemStack itemStack = (ItemStack) this.items.get(1);
            return isFuel(stack) || stack.is(Items.BUCKET) && !itemStack.is(Items.BUCKET);
        }
    }

    @ExpectPlatform
    public static int getCustomBurnDuration(ItemStack stack) {
        throw new AssertionError();
    }

    @Override
    protected int getBurnDuration(ItemStack stack) {
        return getCustomBurnDuration(stack);
    }
}
