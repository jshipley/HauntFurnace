package com.jship.hauntfurnace.block.entity.fabric;

import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.jship.hauntfurnace.data.fabric.FuelMap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;

public class HauntFurnaceBlockEntityImpl {
    public static boolean isFuel(FuelValues fuelValues, ItemStack stack) {
        return (HauntFurnaceConfig.hauntCustomFuels() && FuelMap.HAUNT_FUEL_MAP.containsKey(stack.getItem()))
            || (HauntFurnaceConfig.hauntVanillaFuels() && fuelValues.isFuel(stack));
    }

    public static int getCustomBurnDuration(FuelValues fuelValues, ItemStack stack) {
        int burnDuration = 0;
        if (HauntFurnaceConfig.hauntCustomFuels())
            burnDuration = FuelMap.HAUNT_FUEL_MAP.getOrDefault(stack.getItem(), 0);
        if (burnDuration <= 0 && HauntFurnaceConfig.hauntVanillaFuels())
            burnDuration = fuelValues.burnDuration(stack);
        return burnDuration;
    }
}
