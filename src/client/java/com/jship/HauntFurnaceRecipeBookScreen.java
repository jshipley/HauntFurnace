package com.jship;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class HauntFurnaceRecipeBookScreen extends AbstractFurnaceRecipeBookScreen {
    private static final Text TOGGLE_HAUNTABLE_RECIPES_TEXT = Text.translatable("gui.recipebook.toggleRecipes.hauntable");

    public HauntFurnaceRecipeBookScreen() {
    }

    protected Text getToggleCraftableButtonText() {
        return TOGGLE_HAUNTABLE_RECIPES_TEXT;
    }

    protected Set<Item> getAllowedFuels() {
        return AbstractFurnaceBlockEntity.createFuelTimeMap().keySet();
    }
}
