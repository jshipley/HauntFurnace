package com.jship.hauntfurnace.data.fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.google.gson.JsonParser;
import com.jship.hauntfurnace.HauntFurnace;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@Slf4j
public class FuelDataLoader implements SimpleSynchronousResourceReloadListener {

    private static final ResourceLocation hauntFuelMapLocation = HauntFurnace
            .id("data_maps/item/haunt_furnace_fuels.json");
    private static final ResourceLocation enderFuelMapLocation = HauntFurnace
            .id("data_maps/item/ender_furnace_fuels.json");

    public FuelDataLoader() {
    }

    @Override
    public ResourceLocation getFabricId() {
        return HauntFurnace.id("fuel_map_loader");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        reloadFuelMap(resourceManager, "haunt_furnace_fuels", hauntFuelMapLocation, FuelMap.HAUNT_FUEL_REFERENCE_MAP);
        reloadFuelMap(resourceManager, "ender_furnace_fuels", enderFuelMapLocation, FuelMap.ENDER_FUEL_REFERENCE_MAP);
    }

    public void reloadFuelMap(ResourceManager resourceManager, String fuelMapName, ResourceLocation fuelMapLocation,
            Map<Either<TagKey<Item>, ResourceKey<Item>>, Integer> fuelMap) {
        fuelMap.clear();
        resourceManager.getResourceStack(fuelMapLocation)
                .stream()
                .map((resource) -> {
                    log.debug("Loading fuel map {}", fuelMapLocation);
                    try (BufferedReader reader = resource.openAsReader()) {
                        return FuelMap.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(
                                (message) -> new IOException(
                                        "Error loading fuel map " + fuelMapLocation + ": " + message));
                    } catch (IOException | NoSuchElementException e) {
                        log.error(e.toString());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(map -> {
                    if (map.replace()) {
                        log.warn("{} being replaced by {}", fuelMapName, fuelMapLocation);
                        fuelMap.clear();
                    }
                    map.fuelEntries().entrySet().forEach(entry -> {
                        fuelMap.put(entry.getKey(), entry.getValue().burnTime());
                    });
                });
    }

    public static void resolveFuelMapEntries(RegistryAccess registryAccess, String mapName,
            Map<Either<TagKey<Item>, ResourceKey<Item>>, Integer> fuelReferenceMap, Map<Item, Integer> fuelMap) {
        log.debug("Resolving fuel map: {}", mapName);
        fuelMap.clear();
        val itemRegistry = registryAccess.lookupOrThrow(Registries.ITEM);
        for (var entry : fuelReferenceMap.entrySet()) {
            log.debug("Adding fuel entry: {}", entry.getKey());
            entry.getKey().map(
                    tag -> {
                        val items = itemRegistry.get(tag).orElseThrow(
                                () -> new NoSuchElementException("Unknown tag: " + tag.location()));
                        if (items.size() == 0) {
                            throw new IllegalStateException("Empty tag: " + tag.location());
                        }
                        return items;
                    },
                    key -> List.of(itemRegistry.get(key).orElseThrow(
                            () -> new NoSuchElementException("Unknown item: " + key.location()))))
                    .forEach(holder -> {
                        log.debug("Adding actual fuel: {} ({})", holder.value(),
                                entry.getValue());
                        fuelMap.put(holder.value(), entry.getValue());
                    });
        }
    }
}
