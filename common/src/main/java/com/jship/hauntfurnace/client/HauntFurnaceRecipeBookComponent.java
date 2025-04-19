package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;

import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class HauntFurnaceRecipeBookComponent extends RecipeBookComponent<HauntFurnaceMenu> {

    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
        ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "recipe_book/haunt_furnace_filter_enabled"),
        ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "recipe_book/haunt_furnace_filter_disabled"),
        ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "recipe_book/haunt_furnace_filter_enabled_highlighted"),
        ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "recipe_book/haunt_furnace_filter_disabled_highlighted")
    );
    private static final Component TOGGLE_HAUNTABLE_RECIPES_TEXT = Component.translatable("gui.recipebook.toggleRecipes.hauntable");

    public HauntFurnaceRecipeBookComponent(HauntFurnaceMenu menu, List<RecipeBookComponent.TabInfo> tabInfos) {
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
        ghostSlots.setResult(((HauntFurnaceMenu)this.menu).getResultSlot(), contextMap, recipeDisplay.result());
        if (recipeDisplay instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
            ghostSlots.setInput((Slot)((HauntFurnaceMenu)this.menu).slots.get(0), contextMap, furnaceRecipeDisplay.ingredient());
            Slot slot = (Slot)((HauntFurnaceMenu)this.menu).slots.get(1);
            if (slot.getItem().isEmpty()) {
                ghostSlots.setInput(slot, contextMap, furnaceRecipeDisplay.fuel());
            }
        }
    }

    @Override
    protected Component getRecipeFilterName() {
        return TOGGLE_HAUNTABLE_RECIPES_TEXT;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection possibleRecipes, StackedItemContents stackedItemContents) {
        possibleRecipes.selectRecipes(stackedItemContents, (recipeDisplay) -> {
            return recipeDisplay instanceof FurnaceRecipeDisplay;
        });
    }
}
