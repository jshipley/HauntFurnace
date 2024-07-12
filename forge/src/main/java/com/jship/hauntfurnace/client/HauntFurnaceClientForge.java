package com.jship.hauntfurnace.client;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnaceForge;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HauntFurnace.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HauntFurnaceClientForge {
    @SubscribeEvent
    public static void initClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(HauntFurnaceForge.HAUNT_FURNACE_MENU.get(), HauntFurnaceScreen::new);
            MenuScreens.register(HauntFurnaceForge.POWERED_HAUNT_FURNACE_MENU.get(), PoweredHauntFurnaceScreen::new);
        });
    }
}
