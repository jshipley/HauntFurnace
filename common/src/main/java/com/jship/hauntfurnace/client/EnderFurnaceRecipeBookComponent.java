package com.jship.hauntfurnace.client;

import java.util.Set;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class EnderFurnaceRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    private static final Component TOGGLE_CORRUPTIBLE_RECIPES_TEXT = Component.translatable("gui.recipebook.toggleRecipes.corruptible");
    public EnderFurnaceRecipeBookComponent() {
    }

    @Override
    protected Component getRecipeFilterName() {
      return TOGGLE_CORRUPTIBLE_RECIPES_TEXT;
   }

    @Override
    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
