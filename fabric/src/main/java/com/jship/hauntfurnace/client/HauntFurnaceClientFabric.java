package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.data.fabric.FuelMap;
import com.jship.hauntfurnace.network.Payloads.EnderFurnaceFuelMapS2CPayload;
import com.jship.hauntfurnace.network.Payloads.HauntFurnaceFuelMapS2CPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;

public class HauntFurnaceClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(HauntFurnace.HAUNT_FURNACE_MENU.get(), HauntFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.POWERED_HAUNT_FURNACE_MENU.get(), PoweredHauntFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.ENDER_FURNACE_MENU.get(), EnderFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.POWERED_ENDER_FURNACE_MENU.get(), PoweredEnderFurnaceScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(HauntFurnaceFuelMapS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                FuelMap.HAUNT_FUEL_MAP.clear();
                FuelMap.HAUNT_FUEL_MAP.putAll(payload.fuelMap());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(EnderFurnaceFuelMapS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                FuelMap.ENDER_FUEL_MAP.clear();
                FuelMap.ENDER_FUEL_MAP.putAll(payload.fuelMap());
            });
        });
    }    
}
