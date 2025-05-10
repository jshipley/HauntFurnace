package com.jship.hauntfurnace.recipe;

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

public class HauntingRecipe extends AbstractCookingRecipe {

    private final CookingBookCategory category;

    public HauntingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookTime) {
        super(group, category, ingredient, result, experience, cookTime);
        this.category = category;
    }

    public Item furnaceIcon() {
        return ModBlocks.HAUNT_FURNACE.get().asItem();
    }

    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return ModRecipes.HAUNTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return ModRecipes.HAUNTING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        switch (category) {
            case CookingBookCategory.BLOCKS:
                return ModRecipes.HAUNTING_BLOCKS_CATEGORY.get();
            case CookingBookCategory.FOOD:
                return ModRecipes.HAUNTING_FOOD_CATEGORY.get();
            default:
                return ModRecipes.HAUNTING_MISC_CATEGORY.get();
        }
    }
}
