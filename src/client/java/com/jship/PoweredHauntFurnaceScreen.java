package com.jship;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PoweredHauntFurnaceScreen extends HandledScreen<PoweredHauntFurnaceScreenHandler> {
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
    public final AbstractFurnaceRecipeBookScreen recipeBook;
    private boolean narrow;
    private final Identifier background;
    public static final Identifier TEXTURE = new Identifier(HauntFurnace.MOD_ID, "textures/gui/container/powered_haunt_furnace.png");

    public PoweredHauntFurnaceScreen(PoweredHauntFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.recipeBook = new HauntFurnaceRecipeBookScreen();
        this.background = TEXTURE;
    }

    public void init() {
        super.init();
        this.narrow = this.width < 379;
        this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, (PoweredHauntFurnaceScreenHandler) this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.addDrawableChild(new TexturedButtonWidget(this.x + 29, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (button) -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            button.setPosition(this.x + 29, this.height / 2 - 49);
        }));
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.recipeBook.update();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (this.recipeBook.isOpen() && this.narrow) {
            this.drawBackground(context, delta, mouseX, mouseY);
            this.recipeBook.render(context, mouseX, mouseY, delta);
        } else {
            this.recipeBook.render(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
            this.recipeBook.drawGhostSlots(context, this.x, this.y, true, delta);
        }

        this.drawMouseoverTooltip(context, mouseX, mouseY);
        this.recipeBook.drawTooltip(context, this.x, this.y, mouseX, mouseY);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);

        if (x >= this.x + 9 && x <= this.x + 22 && y >= this.y + 26 && y <= this.y + 59) {
            context.drawTooltip(this.textRenderer,
                    Text.translatable("gui.screen.hauntfurnace.energy",
                            ((PoweredHauntFurnaceScreenHandler) this.handler).energy(),
                            PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY),
                    x, y);
        }
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(this.background, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int energyLevel = ((PoweredHauntFurnaceScreenHandler) this.handler).energyLevel();
        context.drawTexture(this.background, i + 10, j + 27 + 32 - energyLevel, 176, 32 - energyLevel, 12, energyLevel);

        int k = ((PoweredHauntFurnaceScreenHandler) this.handler).getCookProgress();
        context.drawTexture(this.background, i + 79, j + 35, 176, 32, k + 1, 16);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            return this.narrow && this.recipeBook.isOpen() ? true : super.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
        this.recipeBook.slotClicked(slot);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.recipeBook.keyPressed(keyCode, scanCode, modifiers) ? false : super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.backgroundWidth) || mouseY >= (double) (top + this.backgroundHeight);
        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
    }

    public boolean charTyped(char chr, int modifiers) {
        return this.recipeBook.charTyped(chr, modifiers) ? true : super.charTyped(chr, modifiers);
    }

    public void refreshRecipeBook() {
        this.recipeBook.refresh();
    }

    public RecipeBookWidget getRecipeBookWidget() {
        return this.recipeBook;
    }
}
