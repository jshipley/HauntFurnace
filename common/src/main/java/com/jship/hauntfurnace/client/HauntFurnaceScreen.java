package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class HauntFurnaceScreen extends AbstractFurnaceScreen<HauntFurnaceMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(HauntFurnace.MOD_ID, "textures/gui/container/haunt_furnace.png");

    public HauntFurnaceScreen(HauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, new HauntFurnaceRecipeBookComponent(), inventory, title, TEXTURE);
    }
}
