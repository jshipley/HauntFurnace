package com.jship.hauntfurnace.config.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import com.jship.hauntfurnace.config.HauntFurnaceConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> HauntFurnaceConfig.createConfig(parentScreen);
    }
}
