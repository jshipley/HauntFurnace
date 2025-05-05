package com.jship.hauntfurnace.compat.neoforge.rei;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class CorruptingRecipeDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    private float xp;
    private double cookTime;

    public static DisplaySerializer<CorruptingRecipeDisplay> SERIALIZER = DisplaySerializer.of(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(CorruptingRecipeDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(CorruptingRecipeDisplay::getOutputEntries),
                        ResourceLocation.CODEC.optionalFieldOf("location").forGetter(CorruptingRecipeDisplay::getDisplayLocation),
                        Codec.FLOAT.fieldOf("xp").forGetter(display -> display.xp),
                        Codec.DOUBLE.fieldOf("cookTime").forGetter(display -> display.cookTime)
                ).apply(instance, CorruptingRecipeDisplay::new)),
                StreamCodec.composite(
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), CorruptingRecipeDisplay::getInputEntries,
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), CorruptingRecipeDisplay::getOutputEntries,
                        ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), CorruptingRecipeDisplay::getDisplayLocation,
                        ByteBufCodecs.FLOAT, display -> display.xp,
                        ByteBufCodecs.DOUBLE, display -> display.cookTime,
                        CorruptingRecipeDisplay::new));

    public CorruptingRecipeDisplay(RecipeHolder<CorruptingRecipe> recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.value().input())),
            List.of(EntryIngredients.of(recipe.value().result())),
            Optional.of(recipe.id().location()), recipe.value().experience(), recipe.value().cookingTime());
    }

    public CorruptingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Optional<ResourceLocation> id, CompoundTag tag) {
        this(input, output, id, tag.getFloat("xp").orElseThrow(), tag.getDouble("cookTime").orElseThrow());
    }

    public CorruptingRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> id, float xp, double cookTime) {
        super(inputs, outputs);
        this.xp = xp;
        this.cookTime = cookTime;
    }

    @Override
    public int getWidth() {
        return 1;
    }
    
    @Override
    public int getHeight() {
        return 1;
    }

    public OptionalDouble xp() {
        return OptionalDouble.of(xp);
    }

    public OptionalDouble cookTime() {
        return OptionalDouble.of(cookTime);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HauntFurnaceREI.CORRUPTING;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
