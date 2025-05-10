package com.jship.hauntfurnace.client.compat.fabric.rei;

import com.jship.hauntfurnace.HauntFurnace;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.BurningFire;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BurningAltFireWidget extends BurningFire {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "textures/gui/container/rei_display.png");
    public static final ResourceLocation TEXTURE_DARK = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "textures/gui/container/rei_display_dark.png");
    private final int fireShift;
    private Rectangle bounds;
    private double animationDuration = -1;
    private final NumberAnimator<Float> darkBackgroundAlpha = ValueAnimator.ofFloat()
            .withConvention(() -> REIRuntime.getInstance().isDarkThemeEnabled() ? 1.0F : 0.0F, ValueAnimator.typicalTransitionTime())
            .asFloat();

    public BurningAltFireWidget(Rectangle bounds, int fireShift) {
        this.bounds = new Rectangle(Objects.requireNonNull(bounds));
        this.fireShift = fireShift;
    }

    @Override
    public double getAnimationDuration() {
        return animationDuration;
    }

    @Override
    public void setAnimationDuration(double animationDurationMS) {
        this.animationDuration = animationDurationMS;
        if (this.animationDuration <= 0)
            this.animationDuration = -1;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.darkBackgroundAlpha.update(delta);
        renderBackground(guiGraphics, false, 1.0F);
        if (darkBackgroundAlpha.value() > 0) {
            renderBackground(guiGraphics, true, this.darkBackgroundAlpha.value());
        }
    }

    public void renderBackground(GuiGraphics guiGraphics, boolean dark, float alpha) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendFunc(770, 771);
        if (getAnimationDuration() > 0) {
            int height = 14 - Mth.ceil((System.currentTimeMillis() / (animationDuration / 14) % 14d));
            guiGraphics.blit(dark ? TEXTURE_DARK : TEXTURE, getX(), getY(), 1, 74, 14, 14 - height);
            guiGraphics.blit(dark ? TEXTURE_DARK : TEXTURE, getX(), getY() + 14 - height, 82 + (14 * fireShift), 77 + (14 - height), 14, height);
        } else {
            guiGraphics.blit(dark ? TEXTURE_DARK : TEXTURE, getX(), getY(), 1, 74, 14, 14);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
