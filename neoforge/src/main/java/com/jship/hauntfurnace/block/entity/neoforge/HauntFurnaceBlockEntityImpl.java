package com.jship.hauntfurnace.block.entity.neoforge;

import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.jship.hauntfurnace.data.neoforge.FuelData;
import lombok.val;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;

public class HauntFurnaceBlockEntityImpl {
    public static boolean isFuel(FuelValues fuelValues, ItemStack stack) {
        return (HauntFurnaceConfig.hauntCustomFuels() && stack.getItemHolder().getData(FuelData.HAUNT_FUEL_MAP) != null)
            || (HauntFurnaceConfig.hauntVanillaFuels() && stack.getBurnTime(null, fuelValues) > 0);
    }

    public static int getCustomBurnDuration(FuelValues fuelValues, ItemStack stack) {
        int burnDuration = 0;
        if (HauntFurnaceConfig.hauntCustomFuels()) {
            val fuelData = stack.getItemHolder().getData(FuelData.HAUNT_FUEL_MAP);
            burnDuration = fuelData != null ? fuelData.burnTime() : 0;
        }
        if (burnDuration <= 0 && HauntFurnaceConfig.hauntVanillaFuels())
            burnDuration = stack.getBurnTime(null, fuelValues);
        return burnDuration;
    }
}
