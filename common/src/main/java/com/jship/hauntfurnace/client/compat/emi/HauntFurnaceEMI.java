package com.jship.hauntfurnace.client.compat.emi;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class HauntFurnaceEMI implements EmiPlugin {
    public static final ResourceLocation SPRITE_SHEET = HauntFurnace.id("textures/gui/container/jei_gui.png");
    public static final EmiStack HAUNT_FURNACE_WORKSTATION = EmiStack.of(ModBlocks.HAUNT_FURNACE.get());
    public static final EmiStack POWERED_HAUNT_FURNACE_WORKSTATION = EmiStack.of(ModBlocks.POWERED_HAUNT_FURNACE.get());
    public static final EmiRecipeCategory HAUNTING_CATEGORY = new EmiRecipeCategory(HauntFurnace.id("haunt_furnace"), HAUNT_FURNACE_WORKSTATION, HAUNT_FURNACE_WORKSTATION, EmiRecipeSorting.compareOutputThenInput());
    public static final EmiStack ENDER_FURNACE_WORKSTATION = EmiStack.of(ModBlocks.ENDER_FURNACE.get());
    public static final EmiStack POWERED_ENDER_FURNACE_WORKSTATION = EmiStack.of(ModBlocks.POWERED_ENDER_FURNACE.get());
    public static final EmiRecipeCategory CORRUPTING_CATEGORY = new EmiRecipeCategory(HauntFurnace.id("ender_furnace"), ENDER_FURNACE_WORKSTATION, ENDER_FURNACE_WORKSTATION, EmiRecipeSorting.compareOutputThenInput());
    
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(HAUNTING_CATEGORY);
        registry.addWorkstation(HAUNTING_CATEGORY, HAUNT_FURNACE_WORKSTATION);
        registry.addWorkstation(HAUNTING_CATEGORY, POWERED_HAUNT_FURNACE_WORKSTATION);
        registry.addCategory(CORRUPTING_CATEGORY);
        registry.addWorkstation(CORRUPTING_CATEGORY, ENDER_FURNACE_WORKSTATION);
        registry.addWorkstation(CORRUPTING_CATEGORY, POWERED_ENDER_FURNACE_WORKSTATION);
        
        RecipeManager recipeManager = registry.getRecipeManager();

        for(RecipeHolder<HauntingRecipe> recipe : recipeManager.getAllRecipesFor(ModRecipes.HAUNTING_RECIPE.get())) {
            registry.addRecipe(new HauntingEMIRecipe(recipe.value()));
        }

        for(RecipeHolder<CorruptingRecipe> recipe : recipeManager.getAllRecipesFor(ModRecipes.CORRUPTING_RECIPE.get())) {
            registry.addRecipe(new CorruptingEMIRecipe(recipe.value()));
        }
    }
}
