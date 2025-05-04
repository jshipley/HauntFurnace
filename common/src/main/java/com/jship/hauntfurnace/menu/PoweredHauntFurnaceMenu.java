package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class PoweredHauntFurnaceMenu extends AbstractPoweredFurnaceMenu<HauntingRecipe> {

    public PoweredHauntFurnaceMenu(int containerId, Inventory playerInventory) {
        super(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), containerId, playerInventory, HauntFurnace.Recipes.HAUNT_FURNACE_INPUT);
    }

    public PoweredHauntFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), containerId, playerInventory, container, data, HauntFurnace.Recipes.HAUNT_FURNACE_INPUT);
    }
}
