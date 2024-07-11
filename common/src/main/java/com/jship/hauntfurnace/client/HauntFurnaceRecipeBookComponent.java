package com.jship.hauntfurnace.client;

import java.util.Set;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class HauntFurnaceRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    private static final Component TOGGLE_HAUNTABLE_RECIPES_TEXT = Component.translatable("gui.recipebook.toggleRecipes.hauntable");
    public HauntFurnaceRecipeBookComponent() {
    }

    @Override
    protected Component getRecipeFilterName() {
      return TOGGLE_HAUNTABLE_RECIPES_TEXT;
   }

    @Override
    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
