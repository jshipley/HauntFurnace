package com.jship.hauntfurnace.client.fabric;

import com.jship.hauntfurnace.HauntFurnace.ModMenus;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredEnderFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredHauntFurnaceScreen;
import com.jship.hauntfurnace.data.fabric.FuelMap;
import com.jship.hauntfurnace.network.fabric.Payloads.EnderFurnaceFuelMapS2CPayload;
import com.jship.hauntfurnace.network.fabric.Payloads.HauntFurnaceFuelMapS2CPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;

public class HauntFurnaceClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenus.HAUNT_FURNACE.get(), HauntFurnaceScreen::new);
        MenuScreens.register(ModMenus.POWERED_HAUNT_FURNACE.get(), PoweredHauntFurnaceScreen::new);
        MenuScreens.register(ModMenus.ENDER_FURNACE.get(), EnderFurnaceScreen::new);
        MenuScreens.register(ModMenus.POWERED_ENDER_FURNACE.get(), PoweredEnderFurnaceScreen::new);

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
