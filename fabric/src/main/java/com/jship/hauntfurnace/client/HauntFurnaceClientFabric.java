package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class HauntFurnaceClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(HauntFurnace.HAUNT_FURNACE_MENU, HauntFurnaceScreen::new);
        MenuScreens.register(HauntFurnace.POWERED_HAUNT_FURNACE_MENU, PoweredHauntFurnaceScreen::new);
    }    
}
