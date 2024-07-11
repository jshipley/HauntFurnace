package com.jship.hauntfurnace.client.compat.rei;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.minecraft.resources.ResourceLocation;

public class HauntFurnaceREI implements REIClientPlugin {
    private static final ResourceLocation ID = new ResourceLocation(HauntFurnace.MOD_ID, "rei_plugin");
    public static final CategoryIdentifier<HauntingRecipeDisplay> HAUNTING = CategoryIdentifier.of(HauntFurnace.MOD_ID,
            "rei_haunting_category");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HauntingRecipeCategory());
        registry.addWorkstations(HAUNTING, EntryStacks.of(HauntFurnace.HAUNT_FURNACE_BLOCK));
        registry.addWorkstations(HAUNTING, EntryStacks.of(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(HAUNTING, HauntingRecipeDisplay.serializer(HauntingRecipeDisplay::new));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(HauntingRecipe.class, HauntFurnace.HAUNTING_RECIPE, HauntingRecipeDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), HauntFurnaceScreen.class, HAUNTING);
    }

    @Override
    public String getPluginProviderName() {
        return ID.toString();
    }
}
