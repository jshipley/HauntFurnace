package com.jship.hauntfurnace.compat.rei.client.fabric;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.compat.rei.fabric.CorruptingRecipeCategory;
import com.jship.hauntfurnace.compat.rei.fabric.HauntingRecipeCategory;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;

public class HauntFurnaceREIClient implements REIClientPlugin {

    private static final ResourceLocation ID = HauntFurnace.id("rei_client_plugin");
    public static final CategoryIdentifier<HauntingRecipeClientDisplay> HAUNTING = CategoryIdentifier.of(
            HauntFurnace.MOD_ID,
            "rei_haunting_category");
    public static final CategoryIdentifier<CorruptingRecipeClientDisplay> CORRUPTING = CategoryIdentifier.of(
            HauntFurnace.MOD_ID,
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
    public void registerDisplays(DisplayRegistry registry) {
        registry
                .beginRecipeFiller(FurnaceRecipeDisplay.class)
                .filterType(FurnaceRecipeDisplay.TYPE)
                .filter((display, r) -> EntryIngredients.ofSlotDisplay(display.craftingStation()).contains(
                        EntryStacks.of(ModBlocks.HAUNT_FURNACE.get())))
                .fill(HauntingRecipeClientDisplay::new);
        registry
                .beginRecipeFiller(FurnaceRecipeDisplay.class)
                .filterType(FurnaceRecipeDisplay.TYPE)
                .filter((display, r) -> EntryIngredients.ofSlotDisplay(display.craftingStation()).contains(
                        EntryStacks.of(ModBlocks.ENDER_FURNACE.get())))
                .fill(CorruptingRecipeClientDisplay::new);
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
