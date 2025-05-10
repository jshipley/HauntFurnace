package com.jship.hauntfurnace.client.compat.jei;

import java.util.ArrayList;
import java.util.List;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import lombok.extern.slf4j.Slf4j;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
@Slf4j
public class HauntFurnaceJEI implements IModPlugin {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "jei_plugin");
    public static final RecipeType<HauntingRecipe> HAUNTING_RECIPE_TYPE = RecipeType.create(HauntFurnace.MOD_ID, "haunting", HauntingRecipe.class);
    public static final RecipeType<CorruptingRecipe> CORRUPTING_RECIPE_TYPE = RecipeType.create(HauntFurnace.MOD_ID, "corrupting", CorruptingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new HauntingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CorruptingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        
        List<RecipeHolder<HauntingRecipe>> hauntingRecipeHolders = recipeManager.getAllRecipesFor(ModRecipes.HAUNTING_RECIPE.get());
        List<HauntingRecipe> hauntingRecipes = new ArrayList<HauntingRecipe>();
        for(RecipeHolder<HauntingRecipe> recipeHolder : hauntingRecipeHolders) {
            hauntingRecipes.add(recipeHolder.value());
        }

        registration.addRecipes(HAUNTING_RECIPE_TYPE, hauntingRecipes);
        log.info("[Haunt Furnace] Registered haunting recipes: {}", hauntingRecipes.size());

        List<RecipeHolder<CorruptingRecipe>> corruptingRecipeHolders = recipeManager.getAllRecipesFor(ModRecipes.CORRUPTING_RECIPE.get());
        List<CorruptingRecipe> corruptingRecipes = new ArrayList<CorruptingRecipe>();
        for(RecipeHolder<CorruptingRecipe> recipeHolder : corruptingRecipeHolders) {
            corruptingRecipes.add(recipeHolder.value());
        }

        registration.addRecipes(CORRUPTING_RECIPE_TYPE, corruptingRecipes);
        log.info("[Haunt Furnace] Registered corrupting recipes: {}", corruptingRecipes.size());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.HAUNT_FURNACE.get()), HAUNTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_HAUNT_FURNACE.get()), HAUNTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENDER_FURNACE.get()), CORRUPTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.POWERED_ENDER_FURNACE.get()), CORRUPTING_RECIPE_TYPE);
    }
}
