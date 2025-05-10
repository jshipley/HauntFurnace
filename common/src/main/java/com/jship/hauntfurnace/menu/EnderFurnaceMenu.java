package com.jship.hauntfurnace.menu;

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
    public EnderFurnaceMenu(int i, Inventory inventory) {
        super(ModMenus.ENDER_FURNACE_MENU.get(), ModRecipes.CORRUPTING_RECIPE.get(), RecipeBookType.FURNACE, i, inventory);
    }

    public EnderFurnaceMenu(int i, Inventory inventory, Container container, ContainerData containerData) {
        super(ModMenus.ENDER_FURNACE_MENU.get(), ModRecipes.CORRUPTING_RECIPE.get(), RecipeBookType.FURNACE, i, inventory, container, containerData);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
      return EnderFurnaceBlockEntity.isFuel(stack);
   }
}
