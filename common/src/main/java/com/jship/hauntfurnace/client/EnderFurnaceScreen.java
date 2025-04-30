package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnderFurnaceScreen extends AbstractFurnaceScreen<EnderFurnaceMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
    public static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "container/ender_furnace/lit_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public EnderFurnaceScreen(EnderFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, new EnderFurnaceRecipeBookComponent(), inventory, title, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
