package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnaceNeoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = HauntFurnace.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HauntFurnaceClientForge {
    
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(HauntFurnaceNeoforge.HAUNT_FURNACE_MENU.get(), HauntFurnaceScreen::new);
        event.register(HauntFurnaceNeoforge.POWERED_HAUNT_FURNACE_MENU.get(), PoweredHauntFurnaceScreen::new);
        event.register(HauntFurnaceNeoforge.ENDER_FURNACE_MENU.get(), EnderFurnaceScreen::new);
        event.register(HauntFurnaceNeoforge.POWERED_ENDER_FURNACE_MENU.get(), PoweredEnderFurnaceScreen::new);
    }
}
