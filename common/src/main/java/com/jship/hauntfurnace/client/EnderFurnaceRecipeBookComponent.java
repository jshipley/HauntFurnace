package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;
import java.util.List;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class EnderFurnaceRecipeBookComponent extends RecipeBookComponent<EnderFurnaceMenu> {

    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
        HauntFurnace.id("recipe_book/ender_furnace_filter_enabled"),
        HauntFurnace.id("recipe_book/ender_furnace_filter_disabled"),
        HauntFurnace.id("recipe_book/ender_furnace_filter_enabled_highlighted"),
        HauntFurnace.id("recipe_book/ender_furnace_filter_disabled_highlighted")
    );
    private static final Component TOGGLE_CORRUPTIBLE_RECIPES_TEXT = Component.translatable("gui.recipebook.toggleRecipes.corruptible");

    public EnderFurnaceRecipeBookComponent(EnderFurnaceMenu menu, List<RecipeBookComponent.TabInfo> tabInfos) {
        super(menu, tabInfos);
    }

    @Override
    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(FILTER_SPRITES);
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index <= 2;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
        ghostSlots.setResult(((EnderFurnaceMenu) this.menu).getResultSlot(), contextMap, recipeDisplay.result());
        if (recipeDisplay instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
            ghostSlots.setInput((Slot) ((EnderFurnaceMenu) this.menu).slots.get(0), contextMap, furnaceRecipeDisplay.ingredient());
            Slot slot = (Slot) ((EnderFurnaceMenu) this.menu).slots.get(1);
            if (slot.getItem().isEmpty()) {
                ghostSlots.setInput(slot, contextMap, furnaceRecipeDisplay.fuel());
            }
        }
    }

    @Override
    protected Component getRecipeFilterName() {
        return TOGGLE_CORRUPTIBLE_RECIPES_TEXT;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection possibleRecipes, StackedItemContents stackedItemContents) {
        possibleRecipes.selectRecipes(stackedItemContents, recipeDisplay -> {
            return recipeDisplay instanceof FurnaceRecipeDisplay;
        });
    }
}
