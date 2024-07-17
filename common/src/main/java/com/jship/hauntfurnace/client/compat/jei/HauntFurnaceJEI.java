package com.jship.hauntfurnace.client.compat.jei;

import java.util.List;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class HauntFurnaceJEI implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(HauntFurnace.MOD_ID, "jei_plugin");
    public static final RecipeType<HauntingRecipe> HAUNTING_RECIPE_TYPE = RecipeType.create(HauntFurnace.MOD_ID, "haunting", HauntingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        // registration.addRecipeCategories(new HauntingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // HauntFurnace.LOGGER.info("[Haunt Furnace] Attempting to register recipes.");
        // Minecraft minecraft = Minecraft.getInstance();
        // RecipeManager recipeManager = minecraft.level.getRecipeManager();
        // List<HauntingRecipe> recipes = recipeManager.getAllRecipesFor(HauntFurnace.HAUNTING_RECIPE.get());
        // minecraft.close();

        // HauntFurnace.LOGGER.info("[Haunt Furnace] Registered haunting recipes: {}", recipes.size());
        // registration.addRecipes(HAUNTING_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // registration.addRecipeCatalyst(new ItemStack(HauntFurnace.HAUNT_FURNACE_BLOCK.get()), HAUNTING_RECIPE_TYPE);
        // registration.addRecipeCatalyst(new ItemStack(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK.get()), HAUNTING_RECIPE_TYPE);
    }
}
