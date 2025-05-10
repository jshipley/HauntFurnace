package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.EnderFurnaceBlock;
import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredEnderFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.EnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.data.fabric.FuelDataLoader;
import com.jship.hauntfurnace.data.fabric.FuelMap;
import com.jship.hauntfurnace.energy.EnergyStorageFabric;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryFabric;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredEnderFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.network.Payloads;
import com.jship.hauntfurnace.network.Payloads.EnderFurnaceFuelMapS2CPayload;
import com.jship.hauntfurnace.network.Payloads.HauntFurnaceFuelMapS2CPayload;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class HauntFurnaceFabric implements ModInitializer {
    private static Block HAUNT_FURNACE_BLOCK;
    private static Item HAUNT_FURNACE_ITEM;
    private static BlockEntityType<HauntFurnaceBlockEntity> HAUNT_FURNACE_BLOCK_ENTITY;

    private static Block POWERED_HAUNT_FURNACE_BLOCK;
    private static Item POWERED_HAUNT_FURNACE_ITEM;
    private static BlockEntityType<PoweredHauntFurnaceBlockEntity> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

    private static Block ENDER_FURNACE_BLOCK;
    private static Item ENDER_FURNACE_ITEM;
    private static BlockEntityType<EnderFurnaceBlockEntity> ENDER_FURNACE_BLOCK_ENTITY;

    private static Block POWERED_ENDER_FURNACE_BLOCK;
    private static Item POWERED_ENDER_FURNACE_ITEM;
    private static BlockEntityType<PoweredEnderFurnaceBlockEntity> POWERED_ENDER_FURNACE_BLOCK_ENTITY;

    private static Block GILDED_END_STONE_BLOCK;
    private static Item GILDED_END_STONE_ITEM;

    private static RecipeType<HauntingRecipe> HAUNTING_RECIPE;
    private static RecipeSerializer<HauntingRecipe> HAUNTING_RECIPE_SERIALIZER;

    private static RecipeType<CorruptingRecipe> CORRUPTING_RECIPE;
    private static RecipeSerializer<CorruptingRecipe> CORRUPTING_RECIPE_SERIALIZER;

    private static MenuType<HauntFurnaceMenu> HAUNT_FURNACE_MENU;
    private static MenuType<PoweredHauntFurnaceMenu> POWERED_HAUNT_FURNACE_MENU;

    private static MenuType<EnderFurnaceMenu> ENDER_FURNACE_MENU;
    private static MenuType<PoweredEnderFurnaceMenu> POWERED_ENDER_FURNACE_MENU;

    private static EnergyStorageFactory<EnergyStorageWrapper> ENERGY_STORAGE_FACTORY;

    static {
        ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryFabric();

        HAUNT_FURNACE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                new HauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
        HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                BlockEntityType.Builder
                        .of(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK)
                        .build(null));
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), HAUNT_FURNACE_BLOCK_ENTITY);
        HAUNT_FURNACE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                new BlockItem(HAUNT_FURNACE_BLOCK, new Item.Properties()));

        POWERED_HAUNT_FURNACE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
        POWERED_HAUNT_FURNACE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                BlockEntityType.Builder
                        .of(PoweredHauntFurnaceBlockEntity::new,
                                POWERED_HAUNT_FURNACE_BLOCK)
                        .build(null));
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                POWERED_HAUNT_FURNACE_BLOCK_ENTITY);
        POWERED_HAUNT_FURNACE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                new BlockItem(POWERED_HAUNT_FURNACE_BLOCK, new Item.Properties()));
        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity,
                        direction) -> ((EnergyStorageFabric) ((PoweredHauntFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                POWERED_HAUNT_FURNACE_BLOCK_ENTITY);

        ENDER_FURNACE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "ender_furnace"),
                new EnderFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
        ENDER_FURNACE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "ender_furnace"),
                BlockEntityType.Builder
                        .of(EnderFurnaceBlockEntity::new, ENDER_FURNACE_BLOCK)
                        .build(null));
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction), ENDER_FURNACE_BLOCK_ENTITY);
        ENDER_FURNACE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "ender_furnace"),
                new BlockItem(ENDER_FURNACE_BLOCK, new Item.Properties()));

        POWERED_ENDER_FURNACE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_ender_furnace"),
                new PoweredEnderFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
        POWERED_ENDER_FURNACE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_ender_furnace"),
                BlockEntityType.Builder
                        .of(PoweredEnderFurnaceBlockEntity::new, POWERED_ENDER_FURNACE_BLOCK)
                        .build(null));
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                POWERED_ENDER_FURNACE_BLOCK_ENTITY);
        POWERED_ENDER_FURNACE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_ender_furnace"),
                new BlockItem(POWERED_ENDER_FURNACE_BLOCK, new Item.Properties()));
        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity,
                        direction) -> ((EnergyStorageFabric) ((PoweredEnderFurnaceBlockEntity) blockEntity).energyStorage).fabricEnergyStorage,
                POWERED_ENDER_FURNACE_BLOCK_ENTITY);

        GILDED_END_STONE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "gilded_end_stone"),
                new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)));
        GILDED_END_STONE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "gilded_end_stone"),
                new BlockItem(GILDED_END_STONE_BLOCK, new Item.Properties()));

        HAUNTING_RECIPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                new RecipeType<HauntingRecipe>() {
                    @Override
                    public String toString() {
                        return "hauntfurnace:haunting";
                    }
                });
        HAUNTING_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting"),
                new SimpleCookingSerializer<>(HauntingRecipe::new, 200));

        CORRUPTING_RECIPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "corrupting"),
                new RecipeType<CorruptingRecipe>() {
                    @Override
                    public String toString() {
                        return "hauntfurnace:corrupting";
                    }
                });
        CORRUPTING_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "corrupting"),
                new SimpleCookingSerializer<>(CorruptingRecipe::new, 200));

        HAUNT_FURNACE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunt_furnace"),
                new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));
        POWERED_HAUNT_FURNACE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_haunt_furnace"),
                new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                        FeatureFlags.VANILLA_SET));

        ENDER_FURNACE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "ender_furnace"),
                new MenuType<EnderFurnaceMenu>(EnderFurnaceMenu::new, FeatureFlags.VANILLA_SET));
        POWERED_ENDER_FURNACE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "powered_ender_furnace"),
                new MenuType<PoweredEnderFurnaceMenu>(PoweredEnderFurnaceMenu::new,
                        FeatureFlags.VANILLA_SET));
    }

    @Override
    public void onInitialize() {
        HauntFurnace.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            content.addAfter(Items.BLAST_FURNACE, HAUNT_FURNACE_BLOCK,
                    POWERED_HAUNT_FURNACE_BLOCK, ENDER_FURNACE_BLOCK, POWERED_ENDER_FURNACE_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content -> {
            content.addAfter(Items.END_STONE, GILDED_END_STONE_ITEM);
        });

        PayloadTypeRegistry.playS2C().register(Payloads.HauntFurnaceFuelMapS2CPayload.ID, Payloads.HauntFurnaceFuelMapS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(Payloads.EnderFurnaceFuelMapS2CPayload.ID, Payloads.EnderFurnaceFuelMapS2CPayload.CODEC);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(HauntFurnace.id("fuel_map_loader"), (provider) -> new FuelDataLoader(provider));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (!success)
                return;
            var hauntPayload = new HauntFurnaceFuelMapS2CPayload(FuelMap.HAUNT_FUEL_MAP);
            var enderPayload = new EnderFurnaceFuelMapS2CPayload(FuelMap.ENDER_FUEL_MAP);
            server.getPlayerList().getPlayers().forEach(player -> {
                ServerPlayNetworking.send(player, hauntPayload);
                ServerPlayNetworking.send(player, enderPayload);
            });    
        });

        // Store all of the registry references as suppliers so this works for
        // NeoForge/Forge and Fabric
        HauntFurnace.HAUNT_FURNACE_BLOCK = () -> HAUNT_FURNACE_BLOCK;
        HauntFurnace.HAUNT_FURNACE_ITEM = () -> HAUNT_FURNACE_ITEM;
        HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY = () -> HAUNT_FURNACE_BLOCK_ENTITY;

        HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK = () -> POWERED_HAUNT_FURNACE_BLOCK;
        HauntFurnace.POWERED_HAUNT_FURNACE_ITEM = () -> POWERED_HAUNT_FURNACE_ITEM;
        HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY = () -> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;

        HauntFurnace.ENDER_FURNACE_BLOCK = () -> ENDER_FURNACE_BLOCK;
        HauntFurnace.ENDER_FURNACE_ITEM = () -> ENDER_FURNACE_ITEM;
        HauntFurnace.ENDER_FURNACE_BLOCK_ENTITY = () -> ENDER_FURNACE_BLOCK_ENTITY;

        HauntFurnace.POWERED_ENDER_FURNACE_BLOCK = () -> POWERED_ENDER_FURNACE_BLOCK;
        HauntFurnace.POWERED_ENDER_FURNACE_ITEM = () -> POWERED_ENDER_FURNACE_ITEM;
        HauntFurnace.POWERED_ENDER_FURNACE_BLOCK_ENTITY = () -> POWERED_ENDER_FURNACE_BLOCK_ENTITY;

        HauntFurnace.GILDED_END_STONE_BLOCK = () -> GILDED_END_STONE_BLOCK;
        HauntFurnace.GILDED_END_STONE_ITEM = () -> GILDED_END_STONE_ITEM;

        HauntFurnace.HAUNTING_RECIPE = () -> HAUNTING_RECIPE;
        HauntFurnace.HAUNTING_RECIPE_SERIALIZER = () -> HAUNTING_RECIPE_SERIALIZER;

        HauntFurnace.CORRUPTING_RECIPE = () -> CORRUPTING_RECIPE;
        HauntFurnace.CORRUPTING_RECIPE_SERIALIZER = () -> CORRUPTING_RECIPE_SERIALIZER;

        HauntFurnace.HAUNT_FURNACE_MENU = () -> HAUNT_FURNACE_MENU;
        HauntFurnace.POWERED_HAUNT_FURNACE_MENU = () -> POWERED_HAUNT_FURNACE_MENU;

        HauntFurnace.ENDER_FURNACE_MENU = () -> ENDER_FURNACE_MENU;
        HauntFurnace.POWERED_ENDER_FURNACE_MENU = () -> POWERED_ENDER_FURNACE_MENU;

        HauntFurnace.ENERGY_STORAGE_FACTORY = () -> ENERGY_STORAGE_FACTORY;
    }
}
