package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class HauntFurnaceMenu extends AbstractFurnaceMenu {

    public HauntFurnaceMenu(int containerId, Inventory playerInventory) {
        super(
            HauntFurnace.Menus.HAUNT_FURNACE.get(),
            HauntFurnace.Recipes.HAUNTING.get(),
            HauntFurnace.Recipes.HAUNT_FURNACE_INPUT,
            RecipeBookType.FURNACE,
            containerId,
            playerInventory
        );
    }

    public HauntFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(
            HauntFurnace.Menus.HAUNT_FURNACE.get(),
            HauntFurnace.Recipes.HAUNTING.get(),
            HauntFurnace.Recipes.HAUNT_FURNACE_INPUT,
            RecipeBookType.FURNACE,
            containerId,
            playerInventory,
            container,
            containerData
        );
    }
}
