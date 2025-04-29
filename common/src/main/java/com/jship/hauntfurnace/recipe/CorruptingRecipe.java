package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace;
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
        return HauntFurnace.Blocks.ENDER_FURNACE.get().asItem();
    }

    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return HauntFurnace.Recipes.CORRUPTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return HauntFurnace.Recipes.CORRUPTING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        switch (category) {
            case CookingBookCategory.BLOCKS:
                return HauntFurnace.Recipes.CORRUPTING_BLOCKS_CATEGORY.get();
            case CookingBookCategory.FOOD:
                return HauntFurnace.Recipes.CORRUPTING_FOOD_CATEGORY.get();
            default:
                return HauntFurnace.Recipes.CORRUPTING_MISC_CATEGORY.get();
        }
    }
}
