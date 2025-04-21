package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@Mod(HauntFurnace.MOD_ID)
public class HauntFurnaceNeoforge {

    public HauntFurnaceNeoforge(IEventBus modEventBus) {
        HauntFurnace.init();
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerSearchCategories);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(HauntFurnace.Blocks.HAUNT_FURNACE.get());
            event.accept(HauntFurnace.Blocks.POWERED_HAUNT_FURNACE.get());
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get(),
            (blockEntity, context) -> (EnergyStorage) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage
        );
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, HauntFurnace.BlockEntities.HAUNT_FURNACE.get(), (blockEntity, side) -> {
            return new SidedInvWrapper(blockEntity, side);
        });
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get(),
            (blockEntity, side) -> {
                return new SidedInvWrapper(blockEntity, side);
            }
        );
    }

    private void registerSearchCategories(RegisterRecipeBookSearchCategoriesEvent event) {
        event.register(
            HauntFurnace.Recipes.HAUNTING_SEARCH_CATEGORY,
            HauntFurnace.Recipes.HAUNTING_BLOCKS_CATEGORY.get(),
            HauntFurnace.Recipes.HAUNTING_FOOD_CATEGORY.get(),
            HauntFurnace.Recipes.HAUNTING_MISC_CATEGORY.get()
        );
    }
}
