package com.jship.hauntfurnace.compat.neoforge.rei;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.compat.neoforge.rei.client.CorruptingRecipeClientDisplay;
import com.jship.hauntfurnace.compat.neoforge.rei.client.HauntingRecipeClientDisplay;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.forge.REIPluginCommon;
import net.minecraft.resources.ResourceLocation;

@REIPluginCommon
public class HauntFurnaceREI implements REICommonPlugin {
    private static final ResourceLocation ID = HauntFurnace.id("rei_plugin");
    public static final CategoryIdentifier<HauntingRecipeDisplay> HAUNTING = CategoryIdentifier.of(HauntFurnace.MOD_ID, "rei_haunting_category");
    public static final CategoryIdentifier<CorruptingRecipeDisplay> CORRUPTING = CategoryIdentifier.of(HauntFurnace.MOD_ID, "rei_corrupting_category");
    
    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(HauntingRecipe.class)
            .filterType(HauntFurnace.Recipes.HAUNTING.get())
            .fill(HauntingRecipeDisplay::new);
        registry.beginRecipeFiller(CorruptingRecipe.class)
            .filterType(HauntFurnace.Recipes.CORRUPTING.get())
            .fill(CorruptingRecipeDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(HauntFurnace.id("haunting_display"), HauntingRecipeDisplay.SERIALIZER);
        registry.register(HauntFurnace.id("haunting_client_display"), HauntingRecipeClientDisplay.SERIALIZER);
        registry.register(HauntFurnace.id("corrupting_display"), CorruptingRecipeDisplay.SERIALIZER);
        registry.register(HauntFurnace.id("corrupting_client_display"), CorruptingRecipeClientDisplay.SERIALIZER);
    }

    @Override
    public String getPluginProviderName() {
        return ID.toString();
    }
}
