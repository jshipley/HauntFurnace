package com.jship.hauntfurnace.client.compat.fabric.rei;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;

public class HauntFurnaceREI implements REIClientPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "rei_plugin");
    public static final CategoryIdentifier<HauntingRecipeDisplay> HAUNTING = CategoryIdentifier.of(HauntFurnace.MOD_ID,
            "rei_haunting_category");
    public static final CategoryIdentifier<CorruptingRecipeDisplay> CORRUPTING = CategoryIdentifier.of(HauntFurnace.MOD_ID,
            "rei_corrupting_category");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HauntingRecipeCategory());
        registry.addWorkstations(HAUNTING, EntryStacks.of(ModBlocks.HAUNT_FURNACE.get()));
        registry.addWorkstations(HAUNTING, EntryStacks.of(ModBlocks.POWERED_HAUNT_FURNACE.get()));
        registry.add(new CorruptingRecipeCategory());
        registry.addWorkstations(CORRUPTING, EntryStacks.of(ModBlocks.ENDER_FURNACE.get()));
        registry.addWorkstations(CORRUPTING, EntryStacks.of(ModBlocks.POWERED_ENDER_FURNACE.get()));
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(HAUNTING, HauntingRecipeDisplay.serializer(HauntingRecipeDisplay::new));
        registry.register(CORRUPTING, CorruptingRecipeDisplay.serializer(CorruptingRecipeDisplay::new));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(HauntingRecipe.class, ModRecipes.HAUNTING.get(), HauntingRecipeDisplay::new);
        registry.registerRecipeFiller(CorruptingRecipe.class, ModRecipes.CORRUPTING.get(), CorruptingRecipeDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), HauntFurnaceScreen.class, HAUNTING);
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), EnderFurnaceScreen.class, CORRUPTING);
    }

    @Override
    public String getPluginProviderName() {
        return ID.toString();
    }
}
