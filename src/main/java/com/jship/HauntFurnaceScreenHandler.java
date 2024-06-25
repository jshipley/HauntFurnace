package com.jship;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class HauntFurnaceScreenHandler extends AbstractFurnaceScreenHandler {
    public HauntFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(HauntFurnace.HAUNT_FURNACE_SCREEN_HANDLER, HauntFurnace.HAUNTING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public HauntFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(HauntFurnace.HAUNT_FURNACE_SCREEN_HANDLER, HauntFurnace.HAUNTING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
