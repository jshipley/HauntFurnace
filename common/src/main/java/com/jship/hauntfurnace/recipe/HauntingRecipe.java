package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HauntingRecipe extends AbstractCookingRecipe {
    public HauntingRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        super(HauntFurnace.HAUNTING_RECIPE, group, category, input, result, experience, cookTime);
    }

    public ItemStack getToastSymbol() {
      return new ItemStack(HauntFurnace.HAUNT_FURNACE_BLOCK);
   }

   public RecipeSerializer<?> getSerializer() {
      return HauntFurnace.HAUNTING_RECIPE_SERIALIZER;
   }

   public boolean isSpecial() {
    return true;
   }
}
