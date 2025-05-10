package com.jship.hauntfurnace.data.neoforge;

import com.jship.hauntfurnace.HauntFurnace;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public record FuelData(int burnTime) {
    public static final Codec<FuelData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("burn_time").forGetter(FuelData::burnTime)).apply(instance, FuelData::new));
    public static final DataMapType<Item, FuelData> HAUNT_FUEL_MAP = DataMapType
            .builder(HauntFurnace.id("haunt_furnace_fuels"), Registries.ITEM, CODEC)
            .synced(CODEC, false).build();
    public static final DataMapType<Item, FuelData> ENDER_FUEL_MAP = DataMapType
            .builder(HauntFurnace.id("ender_furnace_fuels"), Registries.ITEM, CODEC)
            .synced(CODEC, false).build();
}
