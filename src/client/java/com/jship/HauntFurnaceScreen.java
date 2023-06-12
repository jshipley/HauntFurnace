package com.jship;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HauntFurnaceScreen extends AbstractFurnaceScreen<HauntFurnaceScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(HauntFurnace.MOD_ID, "textures/gui/container/haunt_furnace.png");

    public HauntFurnaceScreen(HauntFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new HauntFurnaceRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
