package com.jship.hauntfurnace.client.fabric;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredEnderFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredHauntFurnaceScreen;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class HauntFurnaceClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(HauntFurnace.Menus.HAUNT_FURNACE.get(), HauntFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), PoweredHauntFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.Menus.ENDER_FURNACE.get(), EnderFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.Menus.POWERED_ENDER_FURNACE.get(), PoweredEnderFurnaceScreen::new);
    }    
}
