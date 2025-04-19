package com.jship.hauntfurnace.compat.fabric.rei;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

import com.jship.hauntfurnace.recipe.HauntingRecipe;
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

public class HauntingRecipeDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    private float xp;
    private double cookTime;

    public static DisplaySerializer<HauntingRecipeDisplay> SERIALIZER = DisplaySerializer.of(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(HauntingRecipeDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(HauntingRecipeDisplay::getOutputEntries),
                        ResourceLocation.CODEC.optionalFieldOf("location").forGetter(HauntingRecipeDisplay::getDisplayLocation),
                        Codec.FLOAT.fieldOf("xp").forGetter(display -> display.xp),
                        Codec.DOUBLE.fieldOf("cookTime").forGetter(display -> display.cookTime)
                ).apply(instance, HauntingRecipeDisplay::new)),
                StreamCodec.composite(
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), HauntingRecipeDisplay::getInputEntries,
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), HauntingRecipeDisplay::getOutputEntries,
                        ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), HauntingRecipeDisplay::getDisplayLocation,
                        ByteBufCodecs.FLOAT, display -> display.xp,
                        ByteBufCodecs.DOUBLE, display -> display.cookTime,
                        HauntingRecipeDisplay::new));

    public HauntingRecipeDisplay(RecipeHolder<HauntingRecipe> recipe) {
        this(List.of(EntryIngredients.ofIngredient(recipe.value().input())),
            List.of(EntryIngredients.of(recipe.value().result())),
            Optional.of(recipe.id().location()), recipe.value().experience(), recipe.value().cookingTime());
    }

    public HauntingRecipeDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Optional<ResourceLocation> id, CompoundTag tag) {
        this(input, output, id, tag.getFloat("xp").orElseThrow(), tag.getDouble("cookTime").orElseThrow());
    }

    public HauntingRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> id, float xp, double cookTime) {
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
        return HauntFurnaceREI.HAUNTING;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
