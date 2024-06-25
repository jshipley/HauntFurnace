package com.jship;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HauntFurnaceScreen extends AbstractFurnaceScreen<HauntFurnaceScreenHandler> {
    public static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/furnace.png");
    public static final Identifier LIT_PROGRESS_TEXTURE = Identifier.of(HauntFurnace.MOD_ID, "container/haunt_furnace/lit_progress");
    public static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress");

    public HauntFurnaceScreen(HauntFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new HauntFurnaceRecipeBookScreen(), inventory, title, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }
}
