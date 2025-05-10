package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace.ModMenus;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class HauntFurnaceMenu extends AbstractFurnaceMenu {

    public HauntFurnaceMenu(int containerId, Inventory playerInventory) {
        super(
                ModMenus.HAUNT_FURNACE.get(),
                ModRecipes.HAUNTING.get(),
                ModRecipes.HAUNT_FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory);
    }

    public HauntFurnaceMenu(int containerId, Inventory playerInventory, Container container,
            ContainerData containerData) {
        super(
                ModMenus.HAUNT_FURNACE.get(),
                ModRecipes.HAUNTING.get(),
                ModRecipes.HAUNT_FURNACE_INPUT,
                RecipeBookType.FURNACE,
                containerId,
                playerInventory,
                container,
                containerData);
    }
}
