package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;
import dev.architectury.platform.Platform;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EnderFurnaceScreen extends AbstractFurnaceScreen<EnderFurnaceMenu> {

    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
    public static final ResourceLocation LIT_PROGRESS_TEXTURE = HauntFurnace.id("container/ender_furnace/lit_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.corruptible");

    public static final List<RecipeBookComponent.TabInfo> TABS = Platform.isNeoForge()
        // Neoforge made it easy to add a search category, so use it
        ? List.of(
            new RecipeBookComponent.TabInfo(
                new ItemStack(HauntFurnace.Blocks.ENDER_FURNACE.get().asItem()),
                Optional.empty(),
                HauntFurnace.Recipes.CORRUPTING_SEARCH_CATEGORY
            ),
            new RecipeBookComponent.TabInfo(Items.END_STONE, HauntFurnace.Recipes.CORRUPTING_BLOCKS_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.CHORUS_FRUIT, HauntFurnace.Recipes.CORRUPTING_FOOD_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.ENDER_PEARL, Items.DRAGON_BREATH, HauntFurnace.Recipes.CORRUPTING_MISC_CATEGORY.get())
        )
        // With fabric, I might need to delve into mixins and mutating Enums to get it to work, so don't bother
        : List.of(
            new RecipeBookComponent.TabInfo(Items.END_STONE, HauntFurnace.Recipes.CORRUPTING_BLOCKS_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.CHORUS_FRUIT, HauntFurnace.Recipes.CORRUPTING_FOOD_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.ENDER_PEARL, Items.DRAGON_BREATH, HauntFurnace.Recipes.CORRUPTING_MISC_CATEGORY.get())
        );

    public EnderFurnaceScreen(EnderFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
