package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public class PoweredHauntFurnaceScreen extends AbstractContainerScreen<PoweredHauntFurnaceMenu> implements RecipeUpdateListener {
    public final PoweredHauntFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    public static final ResourceLocation TEXTURE = HauntFurnace.id("textures/gui/container/powered_haunt_furnace.png");
    public static final ResourceLocation POWERED_PROGRESS_TEXTURE = HauntFurnace.id("container/haunt_furnace/powered_progress");
    public static final ResourceLocation BURN_PROGRESS_TEXTURE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public PoweredHauntFurnaceScreen(PoweredHauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.recipeBookComponent = new PoweredHauntFurnaceRecipeBookComponent(menu, HauntFurnaceScreen.TABS);
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
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

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        this.recipeBookComponent.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(guiGraphics, mouseX, mouseY, this.hoveredSlot);
    }

    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderType::guiTextured, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int energyLevel = ((PoweredHauntFurnaceMenu) this.menu).energyLevel();
        guiGraphics.blit(RenderType::guiTextured, POWERED_PROGRESS_TEXTURE, 12, 32, 0, 32 - energyLevel, i + 10, j + 27 + 32 - energyLevel, energyLevel, 12, energyLevel);

        int cookProgress = ((PoweredHauntFurnaceMenu) this.menu).getHauntProgress();
        guiGraphics.blit(RenderType::guiTextured, BURN_PROGRESS_TEXTURE, 24, 16, 0, 0, i + 79, j + 35, 32, cookProgress + 1, 16);
    }

    protected void renderSlots(GuiGraphics guiGraphics) {
        super.renderSlots(guiGraphics);
        this.recipeBookComponent.renderGhostRecipe(guiGraphics, true);
    }

    public boolean charTyped(char codePoint, int modifiers) {
        return this.recipeBookComponent.charTyped(codePoint, modifiers) ? true : super.charTyped(codePoint, modifiers);
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

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    @Override
    public void fillGhostRecipe(RecipeDisplay recipeDisplay) {
        this.recipeBookComponent.fillGhostRecipe(recipeDisplay);
    }
}
