package com.jship.hauntfurnace;

import org.slf4j.Logger;

import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HauntFurnace {
    // Static members that should be initialized by the loader specific initialization classes

    public static final String MOD_ID = "hauntfurnace";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Block HAUNT_FURNACE_BLOCK;
    public static Item HAUNT_FURNACE_ITEM;
    public static BlockEntityType<HauntFurnaceBlockEntity> HAUNT_FURNACE_BLOCK_ENTITY;

    public static Block POWERED_HAUNT_FURNACE_BLOCK;
    public static Item POWERED_HAUNT_FURNACE_ITEM;
    public static BlockEntityType<PoweredHauntFurnaceBlockEntity> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

    public static RecipeType<HauntingRecipe> HAUNTING_RECIPE;
    public static RecipeSerializer<HauntingRecipe> HAUNTING_RECIPE_SERIALIZER;

    public static MenuType<HauntFurnaceMenu> HAUNT_FURNACE_MENU;
    public static MenuType<PoweredHauntFurnaceMenu> POWERED_HAUNT_FURNACE_MENU;

    public static EnergyStorageFactory<EnergyStorageWrapper> ENERGY_STORAGE_FACTORY;

    public static ResourceLocation INTERACT_WITH_HAUNT_FURNACE = new ResourceLocation(MOD_ID, "interact_with_haunt_furnace");
}
