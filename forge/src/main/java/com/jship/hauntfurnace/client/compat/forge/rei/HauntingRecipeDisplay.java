package com.jship.hauntfurnace.client.compat.forge.rei;

import com.jship.hauntfurnace.recipe.HauntingRecipe;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HauntingRecipeDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    Recipe<?> recipe;
    private float xp;
    private int cookTime;

    public HauntingRecipeDisplay(HauntingRecipe recipe) {
        this(EntryIngredients.ofIngredients(
            recipe.getIngredients()),
            Collections.singletonList(EntryIngredients.of(recipe.getResultItem())),
            recipe,
            recipe.getExperience(),
            recipe.getCookingTime());
    }

    public HauntingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output, CompoundTag tag) {
        this(input, output, RecipeManagerContext.getInstance().byId(tag, "location"),
                tag.getFloat("xp"), tag.getInt("cookTime"));
    }

    public HauntingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Recipe<?> recipe, float xp, int cookTime) {
        super(input, output, Optional.ofNullable(recipe).map(Recipe::getId));
        this.recipe = recipe;
        this.xp = xp;
        this.cookTime = cookTime;
    }

    public float getXp() { return xp; }
    public double getCookingTime() { return cookTime; }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HauntFurnaceREI.HAUNTING;
    }

    public static <R extends HauntingRecipeDisplay> BasicDisplay.Serializer<R> serializer(BasicDisplay.Serializer.RecipeLessConstructor<R> constructor) {
        return BasicDisplay.Serializer.ofRecipeLess(constructor, (display, tag) -> {
            tag.putFloat("xp", display.getXp());
            tag.putDouble("cookTime", display.getCookingTime());
        });
    }
}
