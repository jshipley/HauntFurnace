package com.jship.hauntfurnace.compat.fabric.rei.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

public class HauntingRecipeClientDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    private final Optional<RecipeDisplayId> id;

    public static DisplaySerializer<HauntingRecipeClientDisplay> SERIALIZER = DisplaySerializer.of(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(HauntingRecipeClientDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(HauntingRecipeClientDisplay::getOutputEntries),
                        Codec.INT.xmap(RecipeDisplayId::new, RecipeDisplayId::index).optionalFieldOf("id").forGetter(HauntingRecipeClientDisplay::recipeDisplayId)
                ).apply(instance, HauntingRecipeClientDisplay::new)),
                StreamCodec.composite(
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        HauntingRecipeClientDisplay::getInputEntries,
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        HauntingRecipeClientDisplay::getOutputEntries,
                        ByteBufCodecs.optional(ByteBufCodecs.INT.map(RecipeDisplayId::new, RecipeDisplayId::index)),
                        HauntingRecipeClientDisplay::recipeDisplayId,
                        HauntingRecipeClientDisplay::new
                ), false);

    public HauntingRecipeClientDisplay(FurnaceRecipeDisplay recipe, Optional<RecipeDisplayId> id) {
        this(List.of(EntryIngredients.ofSlotDisplay(recipe.ingredient())),
            List.of(EntryIngredients.ofSlotDisplay(recipe.result())),
            id);
    }

    public HauntingRecipeClientDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<RecipeDisplayId> id) {
        super(inputs, outputs, Optional.empty());
        this.id = id;
    }

    public OptionalDouble xp() { return OptionalDouble.empty(); }
    public OptionalDouble cookTime() { return OptionalDouble.empty(); }

    public Optional<RecipeDisplayId> recipeDisplayId() {
        return id;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HauntFurnaceREIClient.HAUNTING;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }
}
