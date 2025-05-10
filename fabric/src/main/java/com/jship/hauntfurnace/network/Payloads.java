package com.jship.hauntfurnace.network;

import java.util.HashMap;
import java.util.Map;

import com.jship.hauntfurnace.HauntFurnace;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;

public class Payloads {
    public static StreamCodec<RegistryFriendlyByteBuf, Map<Item, Integer>> STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec()),
            ByteBufCodecs.INT,
            256);

    public record HauntFurnaceFuelMapS2CPayload(Map<Item, Integer> fuelMap) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<HauntFurnaceFuelMapS2CPayload> ID = new CustomPacketPayload.Type<>(HauntFurnace.id("haunt_furnace_fuel_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, HauntFurnaceFuelMapS2CPayload> CODEC = StreamCodec.composite(STREAM_CODEC, HauntFurnaceFuelMapS2CPayload::fuelMap, HauntFurnaceFuelMapS2CPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record EnderFurnaceFuelMapS2CPayload(Map<Item, Integer> fuelMap) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<EnderFurnaceFuelMapS2CPayload> ID = new CustomPacketPayload.Type<>(HauntFurnace.id("ender_furnace_fuel_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, EnderFurnaceFuelMapS2CPayload> CODEC = StreamCodec.composite(STREAM_CODEC, EnderFurnaceFuelMapS2CPayload::fuelMap, EnderFurnaceFuelMapS2CPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}