package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class PoweredEnderFurnaceMenu extends AbstractPoweredFurnaceMenu<CorruptingRecipe> {

    public PoweredEnderFurnaceMenu(int containerId, Inventory playerInventory) {
        super(HauntFurnace.Menus.POWERED_ENDER_FURNACE.get(), containerId, playerInventory, HauntFurnace.Recipes.ENDER_FURNACE_INPUT);
    }

    public PoweredEnderFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HauntFurnace.Menus.POWERED_ENDER_FURNACE.get(), containerId, playerInventory, container, data, HauntFurnace.Recipes.ENDER_FURNACE_INPUT);
    }
}
