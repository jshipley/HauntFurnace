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

public class HauntingRecipe extends AbstractCookingRecipe {

    public HauntingRecipe(
        String group,
        CookingBookCategory category,
        Ingredient ingredient,
        ItemStack result,
        float experience,
        int cookTime
    ) {
        super(group, category, ingredient, result, experience, cookTime);
    }

    public Item furnaceIcon() {
        return HauntFurnace.Blocks.HAUNT_FURNACE.get().asItem();
    }

    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return HauntFurnace.Recipes.HAUNTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return HauntFurnace.Recipes.HAUNTING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return HauntFurnace.Recipes.HAUNTING_RECIPE_BOOK_CATEGORY.get();
    }
}
