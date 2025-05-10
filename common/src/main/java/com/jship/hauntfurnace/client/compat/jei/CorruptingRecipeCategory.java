package com.jship.hauntfurnace.client.compat.jei;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class CorruptingRecipeCategory implements IRecipeCategory<CorruptingRecipe> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID,
            "textures/gui/container/jei_gui.png");

    private final IDrawable background;
    private final IDrawableStatic staticFlame;
    private final IDrawableAnimated animatedFlame;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public CorruptingRecipeCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(TEXTURE, 110, 114, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(TEXTURE, 0, 114, 82, 54);
        this.regularCookTime = AbstractFurnaceBlockEntity.BURN_TIME_STANDARD;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.ENDER_FURNACE.get()));
        this.localizedName = Component.translatable("hauntfurnace.action.corrupting");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(TEXTURE, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    private IDrawableAnimated getArrow(CorruptingRecipe recipe) {
        double cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked((int) cookTime);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(CorruptingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
            double mouseY) {
        animatedFlame.draw(guiGraphics, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 24, 18);

        drawExperience(recipe, guiGraphics, 0);
        drawCookTime(recipe, guiGraphics, 45);
    }

    private void drawExperience(CorruptingRecipe recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(experienceString);
            guiGraphics.drawString(font, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    private void drawCookTime(CorruptingRecipe recipe, GuiGraphics guiGraphics, int y) {
        double cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = (int) cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public RecipeType<CorruptingRecipe> getRecipeType() {
        return HauntFurnaceJEI.CORRUPTING_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().getFirst());

        builder.addSlot(OUTPUT, 61, 19)
                .addItemStack(recipe.getResultItem(null));
    }
}
