package com.jship.hauntfurnace.data.fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gson.JsonParser;
import com.jship.hauntfurnace.HauntFurnace;
import com.mojang.serialization.JsonOps;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;

@Slf4j
public class FuelDataLoader implements SimpleSynchronousResourceReloadListener {

    HolderLookup.Provider lookupProvider;

    public FuelDataLoader(HolderLookup.Provider lookupProvider) {
        this.lookupProvider = lookupProvider;
    }

    @Override
    public ResourceLocation getFabricId() {
        return HauntFurnace.id("fuel_map_loader");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        reloadFuelMap(resourceManager, "haunt_furnace_fuels", FuelMap.HAUNT_FUEL_MAP);
        reloadFuelMap(resourceManager, "ender_furnace_fuels", FuelMap.ENDER_FUEL_MAP);
    }

    private void reloadFuelMap(ResourceManager resourceManager, String mapName, Map<Item, Integer> map) {
        map.clear();
        resourceManager.getResourceStack(HauntFurnace.id(mapName).withPrefix("data_maps/item/").withSuffix(".json"))
            .forEach((resource) -> {
                try (BufferedReader reader = resource.openAsReader()) {
                    FuelMap fuel_data = FuelMap.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow();
                    if (fuel_data.replace()) {
                        log.warn("{} being replaced by datapack {}", mapName, resource.sourcePackId());
                        FuelMap.HAUNT_FUEL_MAP.clear();
                    }
                    fuel_data.fuelEntries().entrySet().forEach(entry -> {
                        log.debug("Adding fuel: {}", entry.getKey());
                        val itemRegistry = lookupProvider.lookup(Registries.ITEM).orElseThrow(() -> new IllegalStateException("Unable to read item registry"));
                        entry.getKey().map(
                                tag -> itemRegistry.get(tag).orElseThrow(() -> new NoSuchElementException("Unknown tag: " + tag.location())),
                                key -> List.of(itemRegistry.get(key).orElseThrow(() -> new NoSuchElementException("Unknown item: " + key.location())))
                            ).forEach(holder -> {
                                map.put(holder.value(), entry.getValue().burnTime());
                            });
                    });
                } catch (IOException | IllegalStateException | NoSuchElementException e) {
                    log.error("Error occurred while loading {} from datapack {}", mapName, resource.sourcePackId(), e);
                }
            });                        
    }
}
