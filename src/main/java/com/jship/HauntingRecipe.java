package com.jship;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;

public class HauntingRecipe extends AbstractCookingRecipe {
    private final double cookTime;
    private final float experience;
    private final ItemStack output;
    public HauntingRecipe(Identifier id, String group, CookingRecipeCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(HauntFurnace.HAUNTING_RECIPE, id, group, category, input, output, experience, cookTime);
        this.cookTime = cookTime;
        this.experience = experience;
        this.output = output;
    }

    public double getCookingTime() { return this.cookTime; }

    public float getExperience() { return this.experience; }
    public ItemStack getResultItem() { return this.output; }

    public ItemStack createIcon() {
        return new ItemStack(HauntFurnace.HAUNT_FURNACE_BLOCK);
    }

    public boolean isIgnoredInRecipeBook() { return true; }

    public RecipeSerializer<?> getSerializer() {
        return HauntFurnace.HAUNTING_RECIPE_SERIALIZER;
    }
}
