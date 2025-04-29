package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PoweredHauntFurnaceScreen extends AbstractRecipeBookScreen<PoweredHauntFurnaceMenu> {

    public static final ResourceLocation TEXTURE = HauntFurnace.id("textures/gui/container/powered_haunt_furnace.png");
    public static final ResourceLocation POWERED_PROGRESS_TEXTURE = HauntFurnace.id("container/haunt_furnace/powered_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public PoweredHauntFurnaceScreen(PoweredHauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, new PoweredHauntFurnaceRecipeBookComponent(menu, HauntFurnaceScreen.TABS), inventory, title);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 29, this.height / 2 - 49);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        if (x >= this.leftPos + 9 && x <= this.leftPos + 22 && y >= this.topPos + 26 && y <= this.topPos + 59) {
            guiGraphics.renderTooltip(this.font,
                    Component.translatable("gui.screen.hauntfurnace.energy",
                            ((PoweredHauntFurnaceMenu) this.menu).energy(),
                            PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY),
                    x, y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int energyLevel = ((PoweredHauntFurnaceMenu) this.menu).energyLevel();
        guiGraphics.blitSprite(RenderType::guiTextured, POWERED_PROGRESS_TEXTURE, 12, 32, 0, 32 - energyLevel, i + 10, j + 27 + 32 - energyLevel,  12, energyLevel);

        int cookProgress = ((PoweredHauntFurnaceMenu) this.menu).getHauntProgress();
        guiGraphics.blitSprite(RenderType::guiTextured, BURN_PROGRESS_TEXTURE, 24, 16, 0, 0, i + 79, j + 35, cookProgress + 1, 16);
    }
}
