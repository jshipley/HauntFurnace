package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class PoweredHauntFurnaceScreen extends AbstractContainerScreen<PoweredHauntFurnaceMenu> {
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation(
            "textures/gui/recipe_button.png");
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private final ResourceLocation texture;
    public static final ResourceLocation TEXTURE = new ResourceLocation(HauntFurnace.MOD_ID,
            "textures/gui/container/powered_haunt_furnace.png");

    public PoweredHauntFurnaceScreen(PoweredHauntFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.recipeBookComponent = new HauntFurnaceRecipeBookComponent();
        this.texture = TEXTURE;
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 29, this.height / 2 - 49, 20, 18, 0, 0, 19,
                RECIPE_BUTTON_TEXTURE, (button) -> {
                    this.recipeBookComponent.toggleVisibility();
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    ((ImageButton) button).setPosition(this.leftPos + 29, this.height / 2 - 49);
                }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(poseStack);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(poseStack, delta, mouseX, mouseY);
            this.recipeBookComponent.render(poseStack, mouseX, mouseY, delta);
        } else {
            this.recipeBookComponent.render(poseStack, mouseX, mouseY, delta);
            super.render(poseStack, mouseX, mouseY, delta);
            this.recipeBookComponent.renderGhostRecipe(poseStack, this.leftPos, this.topPos, true, delta);
        }

        this.renderTooltip(poseStack, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(poseStack, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);

        if (x >= this.leftPos + 9 && x <= this.leftPos + 22 && y >= this.topPos + 26 && y <= this.topPos + 59) {
            this.renderTooltip(poseStack,
                    Component.translatable("gui.screen.hauntfurnace.energy",
                            ((PoweredHauntFurnaceMenu) this.menu).energy(),
                            PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY),
                    x, y);
        }
    }

    protected void renderBg(PoseStack poseStack, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        
        int energyLevel = ((PoweredHauntFurnaceMenu) this.menu).energyLevel();
        this.blit(poseStack, i + 10, j + 27 + 32 - energyLevel, 176, 32 - energyLevel, 12, energyLevel);

        int cookProgress = ((PoweredHauntFurnaceMenu) this.menu).getCookProgress();
        this.blit(poseStack, i + 79, j + 35, 176, 32, cookProgress + 1, 16);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? true
                    : super.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected void slotClicked(Slot slot, int slotId, int button, ClickType clickType) {
        super.slotClicked(slot, slotId, button, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.recipeBookComponent.keyPressed(keyCode, scanCode, modifiers) ? false
                : super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.imageWidth)
                || mouseY >= (double) (top + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth,
                this.imageHeight, button) && bl;
    }

    public boolean charTyped(char chr, int modifiers) {
        return this.recipeBookComponent.charTyped(chr, modifiers) ? true : super.charTyped(chr, modifiers);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
