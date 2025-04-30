package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CorruptingRecipe extends AbstractCookingRecipe {
    public CorruptingRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        super(HauntFurnace.CORRUPTING_RECIPE.get(), group, category, input, result, experience, cookTime);
    }

    public ItemStack getToastSymbol() {
      return new ItemStack(HauntFurnace.ENDER_FURNACE_BLOCK.get());
   }

   public RecipeSerializer<?> getSerializer() {
      return HauntFurnace.CORRUPTING_RECIPE_SERIALIZER.get();
   }

   public boolean isSpecial() {
    return true;
   }
}
