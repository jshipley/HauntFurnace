package com.jship.hauntfurnace.block.entity.fabric;

import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.jship.hauntfurnace.data.fabric.FuelMap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class EnderFurnaceBlockEntityImpl {
    // public static boolean isFuel(ItemStack stack) {
    //     return (HauntFurnaceConfig.enderCustomFuels() && FuelMap.ENDER_FUEL_MAP.containsKey(stack.getItem())
    //         || HauntFurnaceConfig.enderVanillaFuels() && AbstractFurnaceBlockEntity.isFuel(stack));
    // }

    public static int getCustomBurnDuration(ItemStack stack) {
        int burnDuration = 0;
        if (HauntFurnaceConfig.enderCustomFuels())
            burnDuration = FuelMap.ENDER_FUEL_MAP.getOrDefault(stack.getItem(), 0);
        // if (burnDuration <= 0 && HauntFurnaceConfig.enderVanillaFuels())
        //     burnDuration = AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack, 0);
        return burnDuration;
    }    
}
