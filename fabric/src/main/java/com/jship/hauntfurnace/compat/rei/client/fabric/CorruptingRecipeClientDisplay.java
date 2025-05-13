package com.jship.hauntfurnace.compat.rei.client.fabric;

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

public class CorruptingRecipeClientDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    private final Optional<RecipeDisplayId> id;

    public static DisplaySerializer<CorruptingRecipeClientDisplay> SERIALIZER = DisplaySerializer.of(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(CorruptingRecipeClientDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(CorruptingRecipeClientDisplay::getOutputEntries),
                        Codec.INT.xmap(RecipeDisplayId::new, RecipeDisplayId::index).optionalFieldOf("id").forGetter(CorruptingRecipeClientDisplay::recipeDisplayId)
                ).apply(instance, CorruptingRecipeClientDisplay::new)),
                StreamCodec.composite(
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        CorruptingRecipeClientDisplay::getInputEntries,
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        CorruptingRecipeClientDisplay::getOutputEntries,
                        ByteBufCodecs.optional(ByteBufCodecs.INT.map(RecipeDisplayId::new, RecipeDisplayId::index)),
                        CorruptingRecipeClientDisplay::recipeDisplayId,
                        CorruptingRecipeClientDisplay::new
                ), false);

    public CorruptingRecipeClientDisplay(FurnaceRecipeDisplay recipe, Optional<RecipeDisplayId> id) {
        this(List.of(EntryIngredients.ofSlotDisplay(recipe.ingredient())),
            List.of(EntryIngredients.ofSlotDisplay(recipe.result())),
            id);
    }

    public CorruptingRecipeClientDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<RecipeDisplayId> id) {
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
        return HauntFurnaceREIClient.CORRUPTING;
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
