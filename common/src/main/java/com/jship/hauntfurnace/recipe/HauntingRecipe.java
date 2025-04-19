package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class HauntingRecipe extends AbstractCookingRecipe {

    public HauntingRecipe(
        String group,
        CookingBookCategory category,
        Ingredient input,
        ItemStack result,
        float experience,
        int cookTime
    ) {
        super(group, category, input, result, experience, cookTime);
    }

    public Item furnaceIcon() {
        return HauntFurnace.HAUNT_FURNACE_BLOCK.get().asItem();
    }

    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return HauntFurnace.HAUNTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return HauntFurnace.HAUNTING_RECIPE.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        // TODO custom recipe book categories?
        return RecipeBookCategories.FURNACE_MISC;
    }
}
