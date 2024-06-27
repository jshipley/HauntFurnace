package com.jship.compat.jei;

import com.jship.compat.jei.HauntFurnaceJEI;
import com.jship.HauntFurnace;
import com.jship.HauntingRecipe;

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
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class HauntingRecipeCategory implements IRecipeCategory<HauntingRecipe> {
    private static final Identifier TEXTURE = Identifier.of(HauntFurnace.MOD_ID, "textures/gui/container/jei_gui.png");

    private final IDrawable background;
    private final IDrawableStatic staticFlame;
    private final IDrawableAnimated animatedFlame;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Text localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
        
    public HauntingRecipeCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(TEXTURE, 96, 114, 14, 14);
		animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = guiHelper.createDrawable(TEXTURE, 0, 114, 82, 54);
        this.regularCookTime = AbstractFurnaceBlockEntity.DEFAULT_COOK_TIME;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(HauntFurnace.HAUNT_FURNACE_BLOCK));
        this.localizedName = Text.translatable("hauntfurnace.action.haunting");
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

    private IDrawableAnimated getArrow(RecipeEntry<HauntingRecipe> RecipeEntry) {
        HauntingRecipe recipe = RecipeEntry.value();
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    public void draw(RecipeEntry<HauntingRecipe> RecipeEntry, IRecipeSlotsView recipeSlotsView, DrawContext context, double mouseX, double mouseY) {
        animatedFlame.draw(context, 1, 20);

        IDrawableAnimated arrow = getArrow(RecipeEntry);
        arrow.draw(context, 24, 18);

        drawExperience(RecipeEntry, context, 0);
        drawCookTime(RecipeEntry, context, 45);
    }

    private void drawExperience(RecipeEntry<HauntingRecipe> RecipeEntry, DrawContext context, int y) {
        HauntingRecipe recipe = RecipeEntry.value();
        float experience = recipe.getExperience();
        if (experience > 0) {
			Text experienceString = Text.translatable("gui.jei.category.smelting.experience", experience);
			MinecraftClient minecraft = MinecraftClient.getInstance();
			TextRenderer textRenderer = minecraft.textRenderer;
			int stringWidth = textRenderer.getWidth(experienceString);
			context.drawText(textRenderer, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	private void drawCookTime(RecipeEntry<HauntingRecipe> RecipeEntry, DrawContext context, int y) {
		HauntingRecipe recipe = RecipeEntry.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Text timeString = Text.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
			MinecraftClient minecraft = MinecraftClient.getInstance();
			TextRenderer textRenderer = minecraft.textRenderer;
			int stringWidth = textRenderer.getWidth(timeString);
			context.drawText(textRenderer, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

    @Override
    public Text getTitle() {
        return localizedName;
    }

    @Override
    public RecipeType<HauntingRecipe> getRecipeType() {
        return HauntFurnaceJEI.HAUNTING_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HauntingRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(INPUT, 1, 1)
            .addIngredients(recipe.getIngredients().getFirst());
        
        builder.addSlot(OUTPUT, 61, 19)
            .addItemStack(RecipeUtil.getResultItem(recipe));
    }

    public boolean isHandled(HauntingRecipe recipe) {
        return !recipe.isIgnoredInRecipeBook();
    }
}
