package com.jship.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.jship.HauntFurnace;
import com.jship.HauntingRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HauntFurnaceJEI implements IModPlugin {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Identifier ID = Identifier.of(HauntFurnace.MOD_ID, "jei_plugin");
    public static final RecipeType<HauntingRecipe> HAUNTING_RECIPE_TYPE = RecipeType.create(HauntFurnace.MOD_ID, "haunting", HauntingRecipe.class);

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new HauntingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = MinecraftClient.getInstance().world.getRecipeManager();
        List<RecipeEntry<HauntingRecipe>> recipeEntries = recipeManager.listAllOfType(HauntFurnace.HAUNTING_RECIPE);
        List<HauntingRecipe> recipes = new ArrayList<HauntingRecipe>();
        for(RecipeEntry<HauntingRecipe> recipeEntry : recipeEntries) {
            recipes.add(recipeEntry.value());
        }

        LOGGER.info("Registered haunting recipes: {}", recipes.size());
        registration.addRecipes(HAUNTING_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(HauntFurnace.HAUNT_FURNACE_BLOCK), HAUNTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK), HAUNTING_RECIPE_TYPE);
    }
}
