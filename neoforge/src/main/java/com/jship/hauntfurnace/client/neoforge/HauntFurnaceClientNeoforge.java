package com.jship.hauntfurnace.client.neoforge;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredEnderFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredHauntFurnaceScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = HauntFurnace.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HauntFurnaceClientNeoforge {
    
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(HauntFurnace.Menus.HAUNT_FURNACE.get(), HauntFurnaceScreen::new);
        event.register(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), PoweredHauntFurnaceScreen::new);
        event.register(HauntFurnace.Menus.ENDER_FURNACE.get(), EnderFurnaceScreen::new);
        event.register(HauntFurnace.Menus.POWERED_ENDER_FURNACE.get(), PoweredEnderFurnaceScreen::new);
    }
}
