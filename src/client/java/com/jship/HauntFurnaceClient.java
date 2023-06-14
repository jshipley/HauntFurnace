package com.jship;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class HauntFurnaceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(HauntFurnace.HAUNT_FURNACE_SCREEN_HANDLER, HauntFurnaceScreen::new);
        HandledScreens.register(HauntFurnace.POWERED_HAUNT_FURNACE_SCREEN_HANDLER, PoweredHauntFurnaceScreen::new);
    }
}