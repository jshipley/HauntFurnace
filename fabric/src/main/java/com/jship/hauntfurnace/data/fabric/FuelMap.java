package com.jship.hauntfurnace.data.fabric;

import java.util.HashMap;
import java.util.Map;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

/**
 * This should read a fuel file that is compatible with NeoForge's datamaps
 * A fuel map should look something like this:
 * {
 * "replace": false,
 * "values": {
 * "minecraft:diamond_block": {
 * "burn_time": 200
 * },
 * "#minecraft:leathers": {
 * "burn_time": 40
 * }
 * }
 * }
 * 
 * "replace" is optional, and should only be set by mod packs, not by mods
 */
public record FuelMap(boolean replace, Map<Either<TagKey<Item>, ResourceKey<Item>>, FuelEntry> fuelEntries) {

    public static final Map<Either<TagKey<Item>, ResourceKey<Item>>, Integer> HAUNT_FUEL_REFERENCE_MAP = new HashMap<>();
    public static final Map<Item, Integer> HAUNT_FUEL_MAP = new HashMap<>();
    public static final Map<Either<TagKey<Item>, ResourceKey<Item>>, Integer> ENDER_FUEL_REFERENCE_MAP = new HashMap<>();
    public static final Map<Item, Integer> ENDER_FUEL_MAP = new HashMap<>();

    public record FuelEntry(int burnTime) {
        public static Codec<FuelEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT
                        .validate(val -> val < 0
                                ? DataResult.error(() -> "value must not be negative")
                                : DataResult.success(val))
                        .fieldOf("burn_time").forGetter(FuelEntry::burnTime))
                .apply(instance, FuelEntry::new));
        public static StreamCodec<RegistryFriendlyByteBuf, FuelEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, FuelEntry::burnTime,
                FuelEntry::new);
    }

    // TAG_OR_VALUE copied from DataMapFile
    private static final Codec<Either<TagKey<Item>, ResourceKey<Item>>> TAG_OR_VALUE = ExtraCodecs.TAG_OR_ELEMENT_ID
            .xmap(
                    to -> to.tag() ? Either.left(TagKey.create(Registries.ITEM, to.id()))
                            : Either.right(ResourceKey.create(Registries.ITEM, to.id())),
                    from -> from.map(t -> new ExtraCodecs.TagOrElementLocation(t.location(), true),
                            r -> new ExtraCodecs.TagOrElementLocation(r.location(), false)));
    public static Codec<FuelMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("replace", false).forGetter(FuelMap::replace),
            Codec.unboundedMap(
                    TAG_OR_VALUE,
                    FuelEntry.CODEC).fieldOf("values").forGetter(FuelMap::fuelEntries))
            .apply(instance, FuelMap::new));
}
