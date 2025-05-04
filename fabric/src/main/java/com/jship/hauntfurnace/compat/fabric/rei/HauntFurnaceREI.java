package com.jship.hauntfurnace.compat.fabric.rei;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.resources.ResourceLocation;

public class HauntFurnaceREI implements REICommonPlugin {

    private static final ResourceLocation ID = HauntFurnace.id("rei_plugin");
    public static final CategoryIdentifier<HauntingRecipeDisplay> HAUNTING = CategoryIdentifier.of(HauntFurnace.MOD_ID, "rei_haunting_category");
    public static final CategoryIdentifier<CorruptingRecipeDisplay> CORRUPTING = CategoryIdentifier.of(HauntFurnace.MOD_ID, "rei_corrupting_cateory");

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(HauntingRecipe.class).filterType(HauntFurnace.Recipes.HAUNTING.get()).fill(HauntingRecipeDisplay::new);
        registry.beginRecipeFiller(CorruptingRecipe.class).filterType(HauntFurnace.Recipes.CORRUPTING.get()).fill(CorruptingRecipeDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(ID, HauntingRecipeDisplay.SERIALIZER);
        registry.register(ID, CorruptingRecipeDisplay.SERIALIZER);
    }
}
