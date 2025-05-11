package com.jship.hauntfurnace.data.fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.gson.JsonParser;
import com.jship.hauntfurnace.HauntFurnace;
import com.mojang.serialization.JsonOps;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

@Slf4j
public class FuelDataLoader extends SimplePreparableReloadListener<Collection<FuelMap>>
        implements IdentifiableResourceReloadListener {

    private final HolderLookup.Provider lookupProvider;
    private final String fuelMapName;
    private final ResourceLocation fuelMapLocation;
    private final Supplier<Map<Item, Integer>> fuelMapSupplier;

    public FuelDataLoader(HolderLookup.Provider lookupProvider, String fuelMapName,
            Supplier<Map<Item, Integer>> fuelMapSupplier) {
        this.lookupProvider = lookupProvider;
        this.fuelMapName = fuelMapName;
        this.fuelMapLocation = HauntFurnace.id(fuelMapName).withPrefix("data_maps/item/").withSuffix(".json");
        this.fuelMapSupplier = fuelMapSupplier;
    }

    @Override
    public ResourceLocation getFabricId() {
        return HauntFurnace.id("fuel_map_loader");
    }

    @Override
    public Collection<ResourceLocation> getFabricDependencies() {
        return List.of(ResourceReloadListenerKeys.TAGS);
    }

    @Override
    protected Collection<FuelMap> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return resourceManager.getResourceStack(fuelMapLocation)
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
                .collect(Collectors.toList());
    }

    @Override
    protected void apply(Collection<FuelMap> fuelMaps, ResourceManager resourceManager, ProfilerFiller profiler) {
        val itemRegistry = lookupProvider.lookupOrThrow(Registries.ITEM);
        fuelMapSupplier.get().clear();
        fuelMaps.forEach((fuelData) -> {
            if (fuelData.replace()) {
                log.warn("{} being replaced by {}", fuelMapName, fuelMapLocation);
                fuelMapSupplier.get().clear();
            }
            fuelData.fuelEntries().entrySet().forEach(entry -> {
                log.debug("Adding fuel entry: {}", entry.getKey());
                try {
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
                                        entry.getValue().burnTime());
                                fuelMapSupplier.get().put(holder.value(), entry.getValue().burnTime());
                            });
                } catch (IllegalStateException | NoSuchElementException e) {
                    log.error("Error occurred while loading {}: {}", fuelMapLocation,
                            e);
                }
            });
        });
    }
}
