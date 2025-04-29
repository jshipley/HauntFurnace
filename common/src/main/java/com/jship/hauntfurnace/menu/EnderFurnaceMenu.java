package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class EnderFurnaceMenu extends AbstractFurnaceMenu {

    public EnderFurnaceMenu(int containerId, Inventory playerInventory) {
        super(
            HauntFurnace.Menus.ENDER_FURNACE.get(),
            HauntFurnace.Recipes.CORRUPTING.get(),
            HauntFurnace.Recipes.ENDER_FURNACE_INPUT,
            RecipeBookType.FURNACE,
            containerId,
            playerInventory
        );
    }

    public EnderFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(
            HauntFurnace.Menus.ENDER_FURNACE.get(),
            HauntFurnace.Recipes.CORRUPTING.get(),
            HauntFurnace.Recipes.ENDER_FURNACE_INPUT,
            RecipeBookType.FURNACE,
            containerId,
            playerInventory,
            container,
            containerData
        );
    }
}
