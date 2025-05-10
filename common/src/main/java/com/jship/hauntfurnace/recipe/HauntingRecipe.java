package com.jship.hauntfurnace.recipe;

import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HauntingRecipe extends AbstractCookingRecipe {
    public HauntingRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        super(ModRecipes.HAUNTING.get(), group, category, input, result, experience, cookTime);
    }

    public ItemStack getToastSymbol() {
      return new ItemStack(ModBlocks.HAUNT_FURNACE.get());
   }

   public RecipeSerializer<?> getSerializer() {
      return ModRecipes.HAUNTING_SERIALIZER.get();
   }

   public boolean isSpecial() {
    return true;
   }
}
