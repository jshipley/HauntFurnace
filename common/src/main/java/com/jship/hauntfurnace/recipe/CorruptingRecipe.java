package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class CorruptingRecipe extends AbstractCookingRecipe {

    private final CookingBookCategory category;

    public CorruptingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookTime) {
        super(group, category, ingredient, result, experience, cookTime);
        this.category = category;
    }

    public Item furnaceIcon() {
        return ModBlocks.ENDER_FURNACE.get().asItem();
    }

    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return ModRecipes.CORRUPTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return ModRecipes.CORRUPTING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        switch (category) {
            case CookingBookCategory.BLOCKS:
                return ModRecipes.CORRUPTING_BLOCKS_CATEGORY.get();
            case CookingBookCategory.FOOD:
                return ModRecipes.CORRUPTING_FOOD_CATEGORY.get();
            default:
                return ModRecipes.CORRUPTING_MISC_CATEGORY.get();
        }
    }
}
