package com.jship.hauntfurnace.block.entity.neoforge;

import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.jship.hauntfurnace.data.neoforge.FuelData;
import lombok.val;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FuelValues;

public class EnderFurnaceBlockEntityImpl {
    public static boolean isFuel(FuelValues fuelValues, ItemStack stack) {
        return (HauntFurnaceConfig.enderCustomFuels() && stack.getItemHolder().getData(FuelData.ENDER_FUEL_MAP) != null)
            || (HauntFurnaceConfig.enderVanillaFuels() && stack.getBurnTime(null, fuelValues) > 0);
    }

    public static int getCustomBurnDuration(FuelValues fuelValues, ItemStack stack) {
        int burnDuration = 0;
        if (HauntFurnaceConfig.enderCustomFuels()) {
            val fuelData = stack.getItemHolder().getData(FuelData.ENDER_FUEL_MAP);
            burnDuration = fuelData != null ? fuelData.burnTime() : 0;
        }
        if (burnDuration <= 0 && HauntFurnaceConfig.enderVanillaFuels())
            burnDuration = stack.getBurnTime(null, fuelValues);
        return burnDuration;
    }
}
