package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModMenus;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.block.entity.EnderFurnaceBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;

public class EnderFurnaceMenu extends AbstractFurnaceMenu {

    public EnderFurnaceMenu(int containerId, Inventory playerInventory) {
        super(
                ModMenus.ENDER_FURNACE.get(),
                ModRecipes.CORRUPTING.get(),
                ModRecipes.ENDER_FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory);
    }

    public EnderFurnaceMenu(int containerId, Inventory playerInventory, Container container,
            ContainerData containerData) {
        super(
                ModMenus.ENDER_FURNACE.get(),
                ModRecipes.CORRUPTING.get(),
                ModRecipes.ENDER_FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                container,
                containerData);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
        return EnderFurnaceBlockEntity.isFuel(this.level.fuelValues(), stack);
    }
}
