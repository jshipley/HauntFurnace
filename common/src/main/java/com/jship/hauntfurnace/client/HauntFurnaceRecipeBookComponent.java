package com.jship.hauntfurnace.client;

import java.util.Set;

import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

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
        return HauntFurnaceBlockEntity.getFuel().keySet();
    }
}
