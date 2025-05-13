package com.jship.hauntfurnace.client;

import java.util.List;
import java.util.Optional;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.HauntFurnace.ModRecipes;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;

import dev.architectury.platform.Platform;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HauntFurnaceScreen extends AbstractFurnaceScreen<HauntFurnaceMenu> {

    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
    public static final ResourceLocation LIT_PROGRESS_TEXTURE = HauntFurnace.id("container/haunt_furnace/lit_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.hauntable");

    public static final List<RecipeBookComponent.TabInfo> TABS = Platform.isNeoForge()
        // Neoforge made it easy to add a search category, so use it
        ? List.of(
            new RecipeBookComponent.TabInfo(
                new ItemStack(ModBlocks.HAUNT_FURNACE.get().asItem()),
                Optional.empty(),
                ModRecipes.HAUNTING_SEARCH_CATEGORY
            ),
            new RecipeBookComponent.TabInfo(Items.BLACKSTONE, Items.SHROOMLIGHT, ModRecipes.HAUNTING_BLOCKS_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.POISONOUS_POTATO, Items.ROTTEN_FLESH, ModRecipes.HAUNTING_FOOD_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.CRIMSON_FUNGUS, Items.RAW_GOLD, ModRecipes.HAUNTING_MISC_CATEGORY.get())
        )
        // With fabric, I might need to delve into mixins and mutating Enums to get it to work, so don't bother
        : List.of(
            new RecipeBookComponent.TabInfo(Items.BLACKSTONE, ModRecipes.HAUNTING_BLOCKS_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.POISONOUS_POTATO, ModRecipes.HAUNTING_FOOD_CATEGORY.get()),
            new RecipeBookComponent.TabInfo(Items.CRIMSON_FUNGUS, ModRecipes.HAUNTING_MISC_CATEGORY.get())
        );

    public HauntFurnaceScreen(HauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE, TABS);
    }
}
