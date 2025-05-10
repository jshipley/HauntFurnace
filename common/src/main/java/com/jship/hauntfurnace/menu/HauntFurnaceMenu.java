package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace.ModMenus;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;

public class HauntFurnaceMenu extends AbstractFurnaceMenu {
    public HauntFurnaceMenu(int i, Inventory inventory) {
        super(ModMenus.HAUNT_FURNACE_MENU.get(), ModRecipes.HAUNTING_RECIPE.get(), RecipeBookType.FURNACE, i, inventory);
    }

    public HauntFurnaceMenu(int i, Inventory inventory, Container container, ContainerData containerData) {
        super(ModMenus.HAUNT_FURNACE_MENU.get(), ModRecipes.HAUNTING_RECIPE.get(), RecipeBookType.FURNACE, i, inventory, container, containerData);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
      return HauntFurnaceBlockEntity.isFuel(stack);
   }
}
