package com.jship.hauntfurnace.compat.fabric.rei.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.compat.fabric.rei.HauntingRecipeCategory;
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

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "rei_client_plugin");
    public static final CategoryIdentifier<HauntingRecipeClientDisplay> HAUNTING = CategoryIdentifier.of(
        HauntFurnace.MOD_ID,
        "rei_haunting_category"
    );

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HauntingRecipeCategory());
        registry.addWorkstations(HAUNTING, EntryStacks.of(HauntFurnace.Blocks.HAUNT_FURNACE.get()));
        // registry.addWorkstations(HAUNTING, EntryStacks.of(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry
            .beginRecipeFiller(FurnaceRecipeDisplay.class)
            .filterType(FurnaceRecipeDisplay.TYPE)
            .filter((display, r) ->
                EntryIngredients.ofSlotDisplay(display.craftingStation()).contains(
                    EntryStacks.of(HauntFurnace.Blocks.HAUNT_FURNACE.get())
                )
            )
            .fill(HauntingRecipeClientDisplay::new);
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
