package com.jship.hauntfurnace.compat.fabric.rei;

import com.jship.hauntfurnace.HauntFurnace;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.BurningFire;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BurningSoulFireWidget extends BurningFire {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "textures/gui/container/rei_display.png");
    public static final ResourceLocation TEXTURE_DARK = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "textures/gui/container/rei_display_dark.png");
    private Rectangle bounds;
    private double animationDuration = -1;
    private final NumberAnimator<Float> darkBackgroundAlpha = ValueAnimator.ofFloat()
            .withConvention(() -> REIRuntime.getInstance().isDarkThemeEnabled() ? 1.0F : 0.0F, ValueAnimator.typicalTransitionTime())
            .asFloat();
    
    public BurningSoulFireWidget(Rectangle bounds) {
        this.bounds = new Rectangle(Objects.requireNonNull(bounds));
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.darkBackgroundAlpha.update(delta);
        renderBackground(graphics, false, 1.0F);
        if (darkBackgroundAlpha.value() > 0) {
            renderBackground(graphics, true, this.darkBackgroundAlpha.value());
        }
    }
    
    public void renderBackground(GuiGraphics graphics, boolean dark, float alpha) {
        ResourceLocation texture = dark ? TEXTURE_DARK : TEXTURE;
        if (getAnimationDuration() > 0) {
            int height = 14 - Mth.ceil((System.currentTimeMillis() / (animationDuration / 14) % 14d));
            graphics.blit(RenderType::guiTextured, texture, getX(), getY(), 1, 74, 14, 14 - height, 256, 256, 0xFFFFFF | (int) (alpha * 255) << 24);
            graphics.blit(RenderType::guiTextured, texture, getX(), getY() + 14 - height, 82, 77 + (14 - height), 14, height, 256, 256, 0xFFFFFF | (int) (alpha * 255) << 24);
        } else {
            graphics.blit(RenderType::guiTextured, texture, getX(), getY(), 1, 74, 14, 14, 256, 256, 0xFFFFFF | (int) (alpha * 255) << 24);
        }
    }
    
    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}