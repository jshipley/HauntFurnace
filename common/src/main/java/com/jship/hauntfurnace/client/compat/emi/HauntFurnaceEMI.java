package com.jship.hauntfurnace.client.compat.emi;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class HauntFurnaceEMI implements EmiPlugin {
    public static final ResourceLocation SPRITE_SHEET = new ResourceLocation(HauntFurnace.MOD_ID, "textures/gui/container/jei_gui.png");
    public static final EmiStack HAUNT_FURNACE_WORKSTATION = EmiStack.of(HauntFurnace.HAUNT_FURNACE_BLOCK);
    public static final EmiStack POWERED_HAUNT_FURNACE_WORKSTATION = EmiStack.of(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK);
    public static final EmiRecipeCategory HAUNTING_CATEGORY = new EmiRecipeCategory(new ResourceLocation(HauntFurnace.MOD_ID, "haunt_furnace"), HAUNT_FURNACE_WORKSTATION, HAUNT_FURNACE_WORKSTATION, EmiRecipeSorting.compareOutputThenInput());
    

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(HAUNTING_CATEGORY);
        registry.addWorkstation(HAUNTING_CATEGORY, HAUNT_FURNACE_WORKSTATION);
        registry.addWorkstation(HAUNTING_CATEGORY, POWERED_HAUNT_FURNACE_WORKSTATION);
        
        RecipeManager recipeManager = registry.getRecipeManager();
        for(HauntingRecipe recipe : recipeManager.getAllRecipesFor(HauntFurnace.HAUNTING_RECIPE)) {
            registry.addRecipe(new HauntingEMIRecipe(recipe));
        }
    }
}
