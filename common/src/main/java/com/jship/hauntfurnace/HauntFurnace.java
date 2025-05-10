package com.jship.hauntfurnace;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.jship.hauntfurnace.block.EnderFurnaceBlock;
import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredEnderFurnaceBlock;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.EnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.menu.EnderFurnaceMenu;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredEnderFurnaceMenu;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

@Slf4j
public class HauntFurnace {
    // Static members that should be initialized by the loader specific
    // initialization classes

    public static final String MOD_ID = "hauntfurnace";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);
    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);

    public static final Registrar<RecipeType<?>> RECIPE_TYPES = MANAGER.get().get(Registries.RECIPE_TYPE);
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = MANAGER.get().get(Registries.RECIPE_SERIALIZER);

    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public class ModBlocks {

        public static final RegistrySupplier<HauntFurnaceBlock> HAUNT_FURNACE = registerBlock(
                id("haunt_furnace"),
                () -> new HauntFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)),
                true);

        public static final RegistrySupplier<PoweredHauntFurnaceBlock> POWERED_HAUNT_FURNACE = registerBlock(
                id("powered_haunt_furnace"),
                () -> new PoweredHauntFurnaceBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)),
                true);

        public static final RegistrySupplier<EnderFurnaceBlock> ENDER_FURNACE = registerBlock(
                id("ender_furnace"),
                () -> new EnderFurnaceBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)
                                .mapColor(MapColor.SAND)),
                true);

        public static final RegistrySupplier<PoweredEnderFurnaceBlock> POWERED_ENDER_FURNACE = registerBlock(
                id("powered_ender_furnace"),
                () -> new PoweredEnderFurnaceBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)
                                .mapColor(MapColor.SAND)),
                true);

        public static final RegistrySupplier<Block> GILDED_END_STONE = registerBlock(
                id("gilded_end_stone"),
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)),
                true);

        private static <T extends Block> RegistrySupplier<T> registerBlock(ResourceLocation id,
                Supplier<T> block,
                boolean registerItem) {
            val blockSupplier = BLOCKS.register(id, block);
            if (registerItem) {
                ITEMS.register(id, () -> new BlockItem(blockSupplier.get(), new Item.Properties()));
            }
            return blockSupplier;
        }

        public static void init() {
        }
    }

    public class ModBlockEntities {

        public static final RegistrySupplier<BlockEntityType<HauntFurnaceBlockEntity>> HAUNT_FURNACE = BLOCK_ENTITY_TYPES
                .register(
                        id("haunt_furnace"),
                        () -> BlockEntityType.Builder.of(HauntFurnaceBlockEntity::new, ModBlocks.HAUNT_FURNACE.get())
                                .build(null));

        public static final RegistrySupplier<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE = BLOCK_ENTITY_TYPES
                .register(
                        id("powered_haunt_furnace"),
                        () -> BlockEntityType.Builder
                                .of(PoweredHauntFurnaceBlockEntity::new, ModBlocks.POWERED_HAUNT_FURNACE.get())
                                .build(null));

        public static final RegistrySupplier<BlockEntityType<EnderFurnaceBlockEntity>> ENDER_FURNACE = BLOCK_ENTITY_TYPES
                .register(
                        id("ender_furnace"),
                        () -> BlockEntityType.Builder.of(EnderFurnaceBlockEntity::new, ModBlocks.ENDER_FURNACE.get())
                                .build(null));

        public static final RegistrySupplier<BlockEntityType<PoweredEnderFurnaceBlockEntity>> POWERED_ENDER_FURNACE = BLOCK_ENTITY_TYPES
                .register(
                        id("powered_ender_furnace"),
                        () -> BlockEntityType.Builder
                                .of(PoweredEnderFurnaceBlockEntity::new, ModBlocks.POWERED_ENDER_FURNACE.get())
                                .build(null));

        public static void init() {
        }
    }

    public class ModRecipes {

        public static final RegistrySupplier<RecipeType<HauntingRecipe>> HAUNTING = RECIPE_TYPES
                .register(id("haunting"), () -> new RecipeType<HauntingRecipe>() {
                    @Override
                    public String toString() {
                        return "hauntfurnace:haunting";
                    }
                });
        public static RegistrySupplier<RecipeSerializer<HauntingRecipe>> HAUNTING_SERIALIZER = RECIPE_SERIALIZERS
                .register(id("haunting"), () -> new SimpleCookingSerializer<HauntingRecipe>(HauntingRecipe::new, 200));

        public static final RegistrySupplier<RecipeType<CorruptingRecipe>> CORRUPTING = RECIPE_TYPES
                .register(id("corrupting"), () -> new RecipeType<CorruptingRecipe>() {
                    @Override
                    public String toString() {
                        return "hauntfurnace:corrupting";
                    }
                });
        public static final RegistrySupplier<RecipeSerializer<CorruptingRecipe>> CORRUPTING_SERIALIZER = RECIPE_SERIALIZERS
                .register(id("corrupting"),
                        () -> new SimpleCookingSerializer<CorruptingRecipe>(CorruptingRecipe::new, 200));

        public static void init() {
        }
    }

    public class ModMenus {
        public static RegistrySupplier<MenuType<HauntFurnaceMenu>> HAUNT_FURNACE = MENUS.register(
                id("haunt_furnace"),
                () -> new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET));
        public static RegistrySupplier<MenuType<PoweredHauntFurnaceMenu>> POWERED_HAUNT_FURNACE = MENUS.register(
                id("powered_haunt_furnace"),
                () -> new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new,
                        FeatureFlags.VANILLA_SET));

        public static RegistrySupplier<MenuType<EnderFurnaceMenu>> ENDER_FURNACE = MENUS.register(
                id("ender_furnace"),
                () -> new MenuType<EnderFurnaceMenu>(EnderFurnaceMenu::new, FeatureFlags.VANILLA_SET));
        public static RegistrySupplier<MenuType<PoweredEnderFurnaceMenu>> POWERED_ENDER_FURNACE = MENUS.register(
                id("powered_ender_furnace"),
                () -> new MenuType<PoweredEnderFurnaceMenu>(PoweredEnderFurnaceMenu::new,
                        FeatureFlags.VANILLA_SET));

        public static void init() {
        }
    }

    public static Supplier<EnergyStorageFactory<EnergyStorageWrapper>> ENERGY_STORAGE_FACTORY;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ModBlocks.init();
        ModBlockEntities.init();
        ModRecipes.init();
        ModMenus.init();
    }
}
