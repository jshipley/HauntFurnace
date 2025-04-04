package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryNeoforge;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(HauntFurnace.MOD_ID)
public class HauntFurnaceNeoforge {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK,
                        HauntFurnace.MOD_ID);
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
                        .create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HauntFurnace.MOD_ID);
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM,
                        HauntFurnace.MOD_ID);
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
                        .create(BuiltInRegistries.RECIPE_TYPE, HauntFurnace.MOD_ID);
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
                        .create(BuiltInRegistries.RECIPE_SERIALIZER, HauntFurnace.MOD_ID);
        public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
                        BuiltInRegistries.MENU,
                        HauntFurnace.MOD_ID);

        public static final Supplier<Block> HAUNT_FURNACE_BLOCK = BLOCKS.register("haunt_furnace",
                        () -> new HauntFurnaceBlock(BlockBehaviour.Properties.of()
                                        .mapColor(MapColor.STONE)
                                        .instrument(NoteBlockInstrument.BASEDRUM)
                                        .requiresCorrectToolForDrops()
                                        .strength(3.5F)
                                        .lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 13
                                                        : 0)));
        public static final Supplier<BlockEntityType<HauntFurnaceBlockEntity>> HAUNT_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
                        .register("haunt_furnace", () -> BlockEntityType.Builder
                                        .of(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK.get()).build(null));
        public static final Supplier<Item> HAUNT_FURNACE_ITEM = ITEMS.register("haunt_furnace",
                        () -> new BlockItem(HAUNT_FURNACE_BLOCK.get(), new Item.Properties()));

        public static final Supplier<Block> POWERED_HAUNT_FURNACE_BLOCK = BLOCKS.register("powered_haunt_furnace",
                        () -> new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.of()
                                        .mapColor(MapColor.STONE)
                                        .instrument(NoteBlockInstrument.BASEDRUM)
                                        .requiresCorrectToolForDrops()
                                        .strength(3.5F)
                                        .lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 13
                                                        : 0)));
        public static final Supplier<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
                        .register("powered_haunt_furnace", () -> BlockEntityType.Builder
                                        .of(PoweredHauntFurnaceBlockEntity::new, POWERED_HAUNT_FURNACE_BLOCK.get())
                                        .build(null));
        public static final Supplier<Item> POWERED_HAUNT_FURNACE_ITEM = ITEMS.register("powered_haunt_furnace",
                        () -> new BlockItem(POWERED_HAUNT_FURNACE_BLOCK.get(), new Item.Properties()));

        public static final Supplier<RecipeType<HauntingRecipe>> HAUNTING_RECIPE = RECIPE_TYPES.register(
                        "haunting",
                        () -> new RecipeType<HauntingRecipe>() {
                                @Override
                                public String toString() {
                                        return "hauntfurnace:haunting";
                                }
                        });
        public static final Supplier<RecipeSerializer<HauntingRecipe>> HAUNTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
                        .register("haunting",
                                        () -> new SimpleCookingSerializer<HauntingRecipe>(HauntingRecipe::new, 200));
        public static final Supplier<MenuType<HauntFurnaceMenu>> HAUNT_FURNACE_MENU = MENU_TYPES.register(
                        "haunt_furnace",
                        () -> new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));;
        public static final Supplier<MenuType<PoweredHauntFurnaceMenu>> POWERED_HAUNT_FURNACE_MENU = MENU_TYPES
                        .register("powered_haunt_furnace",
                                        () -> new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                                                        FeatureFlags.VANILLA_SET));;

        public HauntFurnaceNeoforge(IEventBus modEventBus) {
                modEventBus.addListener(this::addCreative);
                modEventBus.addListener(this::loadComplete);
                modEventBus.addListener(this::registerCapabilities);

                BLOCKS.register(modEventBus);
                BLOCK_ENTITY_TYPES.register(modEventBus);
                ITEMS.register(modEventBus);
                RECIPE_TYPES.register(modEventBus);
                RECIPE_SERIALIZERS.register(modEventBus);
                MENU_TYPES.register(modEventBus);
        }

        private void loadComplete(final FMLLoadCompleteEvent event) {
                HauntFurnace.HAUNT_FURNACE_BLOCK = HAUNT_FURNACE_BLOCK;
                HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY = HAUNT_FURNACE_BLOCK_ENTITY;
                HauntFurnace.HAUNT_FURNACE_ITEM = HAUNT_FURNACE_ITEM;
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK = POWERED_HAUNT_FURNACE_BLOCK;
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY = POWERED_HAUNT_FURNACE_BLOCK_ENTITY;
                HauntFurnace.POWERED_HAUNT_FURNACE_ITEM = POWERED_HAUNT_FURNACE_ITEM;
                HauntFurnace.ENERGY_STORAGE_FACTORY = EnergyStorageFactoryNeoforge::new;
                HauntFurnace.HAUNTING_RECIPE = HAUNTING_RECIPE;
                HauntFurnace.HAUNTING_RECIPE_SERIALIZER = HAUNTING_RECIPE_SERIALIZER;
                HauntFurnace.HAUNT_FURNACE_MENU = HAUNT_FURNACE_MENU;
                HauntFurnace.POWERED_HAUNT_FURNACE_MENU = POWERED_HAUNT_FURNACE_MENU;
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                        event.accept(HAUNT_FURNACE_ITEM.get());
                        event.accept(POWERED_HAUNT_FURNACE_ITEM.get());
                }
        }

        private void registerCapabilities(RegisterCapabilitiesEvent event) {
                event.registerBlockEntity(
                                Capabilities.EnergyStorage.BLOCK,
                                POWERED_HAUNT_FURNACE_BLOCK_ENTITY.get(),
                                (blockEntity, context) -> (EnergyStorage) ((PoweredHauntFurnaceBlockEntity)blockEntity).energyStorage);
                event.registerBlockEntity(
                                Capabilities.ItemHandler.BLOCK,
                                HAUNT_FURNACE_BLOCK_ENTITY.get(),
                                (blockEntity, side) -> { return new SidedInvWrapper(blockEntity, side); });
                event.registerBlockEntity(
                                Capabilities.ItemHandler.BLOCK,
                                POWERED_HAUNT_FURNACE_BLOCK_ENTITY.get(),
                                (blockEntity, side) -> { return new SidedInvWrapper(blockEntity, side); });
        }
}
