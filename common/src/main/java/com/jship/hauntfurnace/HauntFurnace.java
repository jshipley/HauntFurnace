package com.jship.hauntfurnace;

import java.util.function.Supplier;

import org.slf4j.Logger;

import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
// import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
// import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import com.mojang.logging.LogUtils;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HauntFurnace {
    // Static members that should be initialized by the loader specific initialization classes

    public static final String MOD_ID = "hauntfurnace";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Supplier<Block> HAUNT_FURNACE_BLOCK;
    public static Supplier<Item> HAUNT_FURNACE_ITEM;
    public static Supplier<BlockEntityType<HauntFurnaceBlockEntity>> HAUNT_FURNACE_BLOCK_ENTITY;

    // public static Supplier<Block> POWERED_HAUNT_FURNACE_BLOCK;
    // public static Supplier<Item> POWERED_HAUNT_FURNACE_ITEM;
    // public static Supplier<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

    public static Supplier<RecipeType<HauntingRecipe>> HAUNTING_RECIPE;
    public static Supplier<AbstractCookingRecipe.Serializer<HauntingRecipe>> HAUNTING_RECIPE_SERIALIZER;
    public static Supplier<RecipeBookCategory> HAUNTING_RECIPE_BOOK_CATEGORY;

    public static Supplier<MenuType<HauntFurnaceMenu>> HAUNT_FURNACE_MENU;
    // public static Supplier<MenuType<PoweredHauntFurnaceMenu>> POWERED_HAUNT_FURNACE_MENU;

    public static Supplier<EnergyStorageFactory<EnergyStorageWrapper>> ENERGY_STORAGE_FACTORY;
}
