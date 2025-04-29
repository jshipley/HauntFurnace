package com.jship.hauntfurnace.compat.emi;

import com.jship.hauntfurnace.HauntFurnace;
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
    public static final EmiStack HAUNT_FURNACE_WORKSTATION = EmiStack.of(HauntFurnace.HAUNT_FURNACE_BLOCK.get());
    public static final EmiStack POWERED_HAUNT_FURNACE_WORKSTATION = EmiStack.of(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK.get());
    public static final EmiRecipeCategory HAUNTING_CATEGORY = new EmiRecipeCategory(HauntFurnace.id("haunt_furnace"), HAUNT_FURNACE_WORKSTATION, HAUNT_FURNACE_WORKSTATION, EmiRecipeSorting.compareOutputThenInput());
    
    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(HAUNTING_CATEGORY);
        registry.addWorkstation(HAUNTING_CATEGORY, HAUNT_FURNACE_WORKSTATION);
        registry.addWorkstation(HAUNTING_CATEGORY, POWERED_HAUNT_FURNACE_WORKSTATION);
        
        RecipeManager recipeManager = registry.getRecipeManager();

        for(RecipeHolder<HauntingRecipe> recipe : recipeManager.getAllRecipesFor(HauntFurnace.HAUNTING_RECIPE.get())) {
            registry.addRecipe(new HauntingEMIRecipe(recipe.value()));
        }
    }
}
