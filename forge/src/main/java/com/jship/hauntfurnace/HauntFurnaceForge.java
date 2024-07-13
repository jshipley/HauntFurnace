package com.jship.hauntfurnace;

import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyCapabilityProvider;
import com.jship.hauntfurnace.energy.EnergyStorageFactoryForge;
import com.jship.hauntfurnace.items.SidedItemHandlerCapabilityProvider;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(HauntFurnace.MOD_ID)
public class HauntFurnaceForge {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        HauntFurnace.MOD_ID);
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, HauntFurnace.MOD_ID);
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        HauntFurnace.MOD_ID);
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
                        .create(ForgeRegistries.RECIPE_TYPES, HauntFurnace.MOD_ID);
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
                        .create(ForgeRegistries.RECIPE_SERIALIZERS, HauntFurnace.MOD_ID);
        public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
                        ForgeRegistries.MENU_TYPES,
                        HauntFurnace.MOD_ID);

        public static final RegistryObject<Block> HAUNT_FURNACE_BLOCK = BLOCKS.register("haunt_furnace",
                        () -> new HauntFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
        public static final RegistryObject<BlockEntityType<HauntFurnaceBlockEntity>> HAUNT_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
                        .register("haunt_furnace", () -> BlockEntityType.Builder
                                        .of(HauntFurnaceBlockEntity::new, HAUNT_FURNACE_BLOCK.get()).build(null));
        public static final RegistryObject<Item> HAUNT_FURNACE_ITEM = ITEMS.register("haunt_furnace",
                        () -> new BlockItem(HAUNT_FURNACE_BLOCK.get(), new Item.Properties()));

        public static final RegistryObject<Block> POWERED_HAUNT_FURNACE_BLOCK = BLOCKS.register("powered_haunt_furnace",
                        () -> new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
        public static final RegistryObject<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
                        .register("powered_haunt_furnace", () -> BlockEntityType.Builder
                                        .of(PoweredHauntFurnaceBlockEntity::new, POWERED_HAUNT_FURNACE_BLOCK.get())
                                        .build(null));
        public static final RegistryObject<Item> POWERED_HAUNT_FURNACE_ITEM = ITEMS.register("powered_haunt_furnace",
                        () -> new BlockItem(POWERED_HAUNT_FURNACE_BLOCK.get(), new Item.Properties()));

        public static final RegistryObject<RecipeType<HauntingRecipe>> HAUNTING_RECIPE = RECIPE_TYPES.register(
                        "haunting",
                        () -> new RecipeType<HauntingRecipe>() {
                                @Override
                                public String toString() {
                                        return "hauntfurnace:haunting";
                                }
                        });
        public static final RegistryObject<RecipeSerializer<HauntingRecipe>> HAUNTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
                        .register("haunting",
                                        () -> new SimpleCookingSerializer<HauntingRecipe>(HauntingRecipe::new, 200));
        public static final RegistryObject<MenuType<HauntFurnaceMenu>> HAUNT_FURNACE_MENU = MENU_TYPES.register(
                        "haunt_furnace",
                        () -> new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));;
        public static final RegistryObject<MenuType<PoweredHauntFurnaceMenu>> POWERED_HAUNT_FURNACE_MENU = MENU_TYPES
                        .register("powered_haunt_furnace",
                                        () -> new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                                                        FeatureFlags.VANILLA_SET));;

        public HauntFurnaceForge() {
                IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

                modEventBus.addListener(this::addCreative);
                modEventBus.addListener(this::loadComplete);

                net.minecraftforge.common.MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class,
                                HauntFurnaceForge::attachCapabilities);

                BLOCKS.register(modEventBus);
                BLOCK_ENTITY_TYPES.register(modEventBus);
                ITEMS.register(modEventBus);
                RECIPE_TYPES.register(modEventBus);
                RECIPE_SERIALIZERS.register(modEventBus);
                MENU_TYPES.register(modEventBus);
        }

        private void loadComplete(final FMLLoadCompleteEvent event) {
                HauntFurnace.HAUNT_FURNACE_BLOCK = HAUNT_FURNACE_BLOCK.get();
                HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY = HAUNT_FURNACE_BLOCK_ENTITY.get();
                HauntFurnace.HAUNT_FURNACE_ITEM = HAUNT_FURNACE_ITEM.get();
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK = POWERED_HAUNT_FURNACE_BLOCK.get();
                HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY = POWERED_HAUNT_FURNACE_BLOCK_ENTITY.get();
                HauntFurnace.POWERED_HAUNT_FURNACE_ITEM = POWERED_HAUNT_FURNACE_ITEM.get();
                HauntFurnace.ENERGY_STORAGE_FACTORY = new EnergyStorageFactoryForge();
                HauntFurnace.HAUNTING_RECIPE = HAUNTING_RECIPE.get();
                HauntFurnace.HAUNTING_RECIPE_SERIALIZER = HAUNTING_RECIPE_SERIALIZER.get();
                HauntFurnace.HAUNT_FURNACE_MENU = HAUNT_FURNACE_MENU.get();
                HauntFurnace.POWERED_HAUNT_FURNACE_MENU = POWERED_HAUNT_FURNACE_MENU.get();
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                        event.accept(HAUNT_FURNACE_ITEM);
                        event.accept(POWERED_HAUNT_FURNACE_ITEM);
                }
        }

        private static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
                if (event.getObject() instanceof PoweredHauntFurnaceBlockEntity) {
                        event.addCapability(
                                        new ResourceLocation(HauntFurnace.MOD_ID,
                                                        "powered_haunt_furnace__capability"),
                                        new SidedItemHandlerCapabilityProvider((PoweredHauntFurnaceBlockEntity) event.getObject()));
                        
                        event.addCapability(
                                        new ResourceLocation(HauntFurnace.MOD_ID,
                                                        "powered_haunt_furnace_energy_capability"),
                                        new EnergyCapabilityProvider(
                                                        (PoweredHauntFurnaceBlockEntity) event.getObject()));
                        
                }
        }
}
