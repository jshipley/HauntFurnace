package com.jship.hauntfurnace;

import com.google.common.base.Suppliers;
import com.jship.hauntfurnace.block.HauntFurnaceBlock;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageFactory;
// import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.menu.HauntFurnaceMenu;
// import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.PlatformOnly;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;

public class HauntFurnace {

    public static final String MOD_ID = "hauntfurnace";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);
    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);

    public static final Registrar<RecipeType<?>> RECIPE_TYPES = MANAGER.get().get(Registries.RECIPE_TYPE);
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = MANAGER.get().get(Registries.RECIPE_SERIALIZER);
    public static final Registrar<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = MANAGER.get().get(Registries.RECIPE_BOOK_CATEGORY);

    public static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);

    public class Blocks {

        public static final RegistrySupplier<HauntFurnaceBlock> HAUNT_FURNACE = registerBlock(
            id("haunt_furnace"),
            () ->
                new HauntFurnaceBlock(
                    BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .instrument(NoteBlockInstrument.BASEDRUM)
                        .requiresCorrectToolForDrops()
                        .strength(3.5F)
                        .lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 13 : 0)
                        .setId(blockKey(id("haunt_furnace")))
                ),
            true
        );

        // public static final Supplier<Block> POWERED_HAUNT_FURNACE_BLOCK = BLOCKS.register("powered_haunt_furnace",
        //                 () -> new PoweredHauntFurnaceBlock(BlockBehaviour.Properties.of()
        //                                 .mapColor(MapColor.STONE)
        //                                 .instrument(NoteBlockInstrument.BASEDRUM)
        //                                 .requiresCorrectToolForDrops()
        //                                 .strength(3.5F)
        //                                 .lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 13
        //                                                 : 0)));
        // public static final Supplier<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
        //                 .register("powered_haunt_furnace", () -> BlockEntityType.Builder
        //                                 .of(PoweredHauntFurnaceBlockEntity::new, POWERED_HAUNT_FURNACE_BLOCK.get())
        //                                 .build(null));

        public static void init() {}
    }

    public class BlockEntities {

        public static final RegistrySupplier<BlockEntityType<HauntFurnaceBlockEntity>> HAUNT_FURNACE = BLOCK_ENTITY_TYPES.register(
            id("haunt_furnace"),
            () -> new BlockEntityType(HauntFurnaceBlockEntity::new, Set.of(Blocks.HAUNT_FURNACE.get()))
        );

        // public static Supplier<BlockEntityType<PoweredHauntFurnaceBlockEntity>> POWERED_HAUNT_FURNACE_BLOCK_ENTITY;
        public static void init() {}
    }

    public class Recipes {

        public static final RegistrySupplier<RecipeType<HauntingRecipe>> HAUNTING = RECIPE_TYPES.register(id("haunting"), () ->
            new RecipeType<HauntingRecipe>() {
                @Override
                public String toString() {
                    return "hauntfurnace:haunting";
                }
            }
        );
        public static final RegistrySupplier<AbstractCookingRecipe.Serializer<HauntingRecipe>> HAUNTING_SERIALIZER =
            RECIPE_SERIALIZERS.register(id("haunting"), () -> new AbstractCookingRecipe.Serializer<HauntingRecipe>(HauntingRecipe::new, 200)
            );
        public static final RegistrySupplier<RecipeBookCategory> HAUNTING_RECIPE_BOOK_CATEGORY = RECIPE_BOOK_CATEGORIES.register(
            id("haunting"),
            RecipeBookCategory::new
        );

        public static ResourceKey<RecipePropertySet> HAUNT_FURNACE_INPUT = ResourceKey.create(
            RecipePropertySet.TYPE_KEY,
            HauntFurnace.id("haunting_input")
        );

        public static void init() {}
    }

    public class Menus {

        public static final RegistrySupplier<MenuType<HauntFurnaceMenu>> HAUNT_FURNACE = MENUS.register(id("haunt_furnace"), () ->
            new MenuType<HauntFurnaceMenu>(HauntFurnaceMenu::new, FeatureFlags.VANILLA_SET)
        );

        // public static final RegistrySupplier<MenuType<HauntFurnaceMenu>> POWERED_HAUNT_FURNACE_MENU = MENUS.register(
        //     id("powered_haunt_furnace"),
        //     () -> new MenuType<PoweredHauntFurnaceMenu>(PoweredHauntFurnaceMenu::new, FeatureFlags.VANILLA_SET)
        // );
        public static void init() {}
    }

    public static Supplier<EnergyStorageFactory<EnergyStorageWrapper>> ENERGY_STORAGE_FACTORY;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static ResourceKey<Block> blockKey(ResourceLocation id) {
        return ResourceKey.create(Registries.BLOCK, id);
    }

    public static ResourceKey<Item> itemKey(ResourceLocation id) {
        return ResourceKey.create(Registries.ITEM, id);
    }

    private static <T extends Block> RegistrySupplier<T> registerBlock(ResourceLocation id, Supplier<T> block, boolean registerItem) {
        RegistrySupplier<T> blockSupplier = BLOCKS.register(id, block);
        if (registerItem) {
            //.arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            ITEMS.register(id, () -> new BlockItem(blockSupplier.get(), new Item.Properties().setId(itemKey(id))));
        }
        return blockSupplier;
    }

    public static void init() {
        Blocks.init();
        BlockEntities.init();
        Recipes.init();
        Menus.init();
    }
}
