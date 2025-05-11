package com.jship.hauntfurnace.client.neoforge;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModMenus;
import com.jship.hauntfurnace.client.EnderFurnaceScreen;
import com.jship.hauntfurnace.client.HauntFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredEnderFurnaceScreen;
import com.jship.hauntfurnace.client.PoweredHauntFurnaceScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = HauntFurnace.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HauntFurnaceClientNeoforge {
    
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.HAUNT_FURNACE.get(), HauntFurnaceScreen::new);
        event.register(ModMenus.POWERED_HAUNT_FURNACE.get(), PoweredHauntFurnaceScreen::new);
        event.register(ModMenus.ENDER_FURNACE.get(), EnderFurnaceScreen::new);
        event.register(ModMenus.POWERED_ENDER_FURNACE.get(), PoweredEnderFurnaceScreen::new);
    }
}
