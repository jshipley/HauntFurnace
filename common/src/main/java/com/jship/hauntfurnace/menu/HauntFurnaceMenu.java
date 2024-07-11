package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class HauntFurnaceMenu extends AbstractFurnaceMenu {
    public HauntFurnaceMenu(int i, Inventory inventory) {
        super(HauntFurnace.HAUNT_FURNACE_MENU, HauntFurnace.HAUNTING_RECIPE, RecipeBookType.FURNACE, i, inventory);
    }

    public HauntFurnaceMenu(int i, Inventory inventory, Container container, ContainerData containerData) {
        super(HauntFurnace.HAUNT_FURNACE_MENU, HauntFurnace.HAUNTING_RECIPE, RecipeBookType.FURNACE, i, inventory, container, containerData);
    }
}
