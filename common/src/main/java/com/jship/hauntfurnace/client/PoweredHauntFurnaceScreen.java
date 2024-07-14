package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class PoweredHauntFurnaceScreen extends AbstractContainerScreen<PoweredHauntFurnaceMenu> implements RecipeUpdateListener {
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "textures/gui/container/powered_haunt_furnace.png");
    public static final ResourceLocation POWERED_PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "container/haunt_furnace/powered_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public PoweredHauntFurnaceScreen(PoweredHauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.recipeBookComponent = new HauntFurnaceRecipeBookComponent();
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 29, this.height / 2 - 49, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, (button) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + 29, this.height / 2 - 49);
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(guiGraphics, delta, mouseX, mouseY);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, delta);
        } else {
            super.render(guiGraphics, mouseX, mouseY, delta);
            this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, delta);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics, this.leftPos, this.topPos, true, delta);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, this.leftPos, this.topPos, mouseX, mouseY);
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

    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int energyLevel = ((PoweredHauntFurnaceMenu) this.menu).energyLevel();
        guiGraphics.blitSprite(POWERED_PROGRESS_TEXTURE, i + 10, j + 27 + 32 - energyLevel, 32 - energyLevel, 12, energyLevel);

        int k = ((PoweredHauntFurnaceMenu) this.menu).getCookProgress();
        guiGraphics.blitSprite(BURN_PROGRESS_TEXTURE, i + 79, j + 35, 32, k + 1, 16);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.widthTooNarrow && this.recipeBookComponent.isVisible()) {
            return true;
         }
         return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void slotClicked(Slot slot, int slotId, int button, ClickType clickType) {
        super.slotClicked(slot, slotId, button, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers)) {
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.imageWidth) || mouseY >= (double) (top + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, button) && bl;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (this.recipeBookComponent.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
