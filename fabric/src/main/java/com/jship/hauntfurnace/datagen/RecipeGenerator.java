package com.jship.hauntfurnace.datagen;

import java.util.concurrent.CompletableFuture;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlocks;
import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class RecipeGenerator extends FabricRecipeProvider {

    public RecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    public void buildRecipes(RecipeOutput output) {
        // Furnaces
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HAUNT_FURNACE.get().asItem())
            .pattern("BBB")
            .pattern("BSB")
            .pattern("BBB")
            .define('B', of(Items.BLACKSTONE))
            .define('S', of(ItemTags.SOUL_FIRE_BASE_BLOCKS))
            .unlockedBy("has_soul_fire", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
            .unlockedBy(getHasName(Items.BLACKSTONE), has(Items.BLACKSTONE))
            .save(output, HauntFurnace.id("crafting/haunt_furnace"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.POWERED_HAUNT_FURNACE.get().asItem())
            .pattern("GGG")
            .pattern("GHG")
            .pattern("GRG")
            .define('G', Ingredient.of(Items.BLACKSTONE))
            .define('H', Ingredient.of(ModBlocks.HAUNT_FURNACE.get().asItem()))
            .define('R', ConventionalItemTags.REDSTONE_DUSTS)
            .unlockedBy(getHasName(ModBlocks.HAUNT_FURNACE.get().asItem()), has(ModBlocks.HAUNT_FURNACE.get().asItem()))
            .save(output, HauntFurnace.id("crafting/powered_haunt_furnace"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ENDER_FURNACE.get().asItem())
            .pattern("EEE")
            .pattern("EDE")
            .pattern("EEE")
            .define('E', of(ConventionalItemTags.END_STONES))
            .define('D', of(Items.DRAGON_HEAD))
            .unlockedBy(getHasName(Items.DRAGON_HEAD), has(Items.DRAGON_HEAD))
            .save(output, HauntFurnace.id("crafting/ender_furnace"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.POWERED_ENDER_FURNACE.get().asItem())
            .pattern("GGG")
            .pattern("GEG")
            .pattern("GRG")
            .define('G', of(ModBlocks.GILDED_END_STONE.get()))
            .define('E', of(ModBlocks.ENDER_FURNACE.get()))
            .define('R', of(ConventionalItemTags.REDSTONE_DUSTS))
            .unlockedBy(getHasName(ModBlocks.ENDER_FURNACE.get()), has(ModBlocks.ENDER_FURNACE.get()))
            .save(output, HauntFurnace.id("crafting/powered_ender_furnace"));

        // Gilding
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.GILDED_BLACKSTONE)
            .pattern("NNN")
            .pattern("NBN")
            .pattern("NNN")
            .define('N', of(ConventionalItemTags.GOLD_NUGGETS))
            .define('B', of(Items.BLACKSTONE))
            .unlockedBy(getHasName(Items.BLACKSTONE), has(Items.BLACKSTONE))
            .save(output, HauntFurnace.id("crafting/gilded_blackstone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GILDED_END_STONE.get())
            .pattern("NNN")
            .pattern("NEN")
            .pattern("NNN")
            .define('N', of(ConventionalItemTags.GOLD_NUGGETS))
            .define('E', of(Items.END_STONE))
            .unlockedBy(getHasName(Items.END_STONE), has(Items.END_STONE))
            .save(output, HauntFurnace.id("crafting/gilded_end_stone"));

        // Haunting recipes
        offerHaunting(output, "blackstone", CookingBookCategory.BLOCKS, of(ConventionalItemTags.COBBLESTONES), new ItemStack(Items.BLACKSTONE), 0.1f, 200);
        offerHaunting(output, "crimson_fungus", CookingBookCategory.MISC, of(Items.RED_MUSHROOM), new ItemStack(Items.CRIMSON_FUNGUS), 0.2f, 200);
        offerHaunting(output, "crimson_stem", CookingBookCategory.MISC, of(ItemTagGenerator.RED_WOODS), new ItemStack(Items.CRIMSON_STEM), 0.2f, 200);
        offerHaunting(output, "crying_obsidian", CookingBookCategory.BLOCKS, of(ConventionalItemTags.NORMAL_OBSIDIANS), new ItemStack(Items.CRYING_OBSIDIAN), 0.4f, 200);
        offerHaunting(output, "dead_bush", CookingBookCategory.MISC, of(ItemTags.SAPLINGS), new ItemStack(Items.DEAD_BUSH), 0.2f, 200);
        offerHaunting(output, "ender_eye", CookingBookCategory.MISC, of(Items.ENDER_PEARL), new ItemStack(Items.ENDER_EYE), 0.4f, 200);
        offerHaunting(output, "gold_ingot", CookingBookCategory.MISC, of(ConventionalItemTags.COPPER_INGOTS), new ItemStack(Items.GOLD_INGOT), 1.0f, 200);
        offerHaunting(output, "glow_berries", CookingBookCategory.FOOD, of(ConventionalItemTags.BERRY_FOODS), new ItemStack(Items.GLOW_BERRIES), 0.2f, 200);
        offerHaunting(output, "glow_ink_sac", CookingBookCategory.MISC, of(Items.INK_SAC), new ItemStack(Items.GLOW_INK_SAC), 0.3f, 200);
        offerHaunting(output, "glowstone", CookingBookCategory.MISC, of(Items.REDSTONE), new ItemStack(Items.GLOWSTONE), 0.7f, 200);
        offerHaunting(output, "glowstone_dust", CookingBookCategory.MISC, of(ConventionalItemTags.REDSTONE_DUSTS), new ItemStack(Items.GLOWSTONE_DUST), 0.4f, 200);
        offerHaunting(output, "infested_chiseled_stone_bricks", CookingBookCategory.BLOCKS, of(Items.CHISELED_STONE_BRICKS), new ItemStack(Items.INFESTED_CHISELED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(output, "infested_cracked_stone_bricks", CookingBookCategory.BLOCKS, of(Items.CRACKED_STONE_BRICKS), new ItemStack(Items.INFESTED_CRACKED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(output, "infested_deepslate", CookingBookCategory.BLOCKS, of(Items.DEEPSLATE), new ItemStack(Items.INFESTED_DEEPSLATE), 0.1f, 200);
        offerHaunting(output, "infested_mossy_stone_bricks", CookingBookCategory.BLOCKS, of(Items.MOSSY_STONE_BRICKS), new ItemStack(Items.INFESTED_MOSSY_STONE_BRICKS), 0.1f, 200);
        offerHaunting(output, "infested_stone", CookingBookCategory.BLOCKS, of(Items.STONE), new ItemStack(Items.INFESTED_STONE), 0.1f, 200);
        offerHaunting(output, "infested_stone_bricks", CookingBookCategory.BLOCKS, of(Items.STONE_BRICKS), new ItemStack(Items.INFESTED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(output, "nether_brick", CookingBookCategory.MISC, of(Items.BRICK), new ItemStack(Items.NETHER_BRICK), 0.1f, 200);
        offerHaunting(output, "nether_bricks", CookingBookCategory.BLOCKS, of(Items.BRICKS), new ItemStack(Items.NETHER_BRICKS), 0.1f, 200);
        offerHaunting(output, "nether_wart", CookingBookCategory.MISC, of(ConventionalItemTags.SEEDS), new ItemStack(Items.NETHER_WART), 0.2f, 200);
        offerHaunting(output, "nether_wart_block", CookingBookCategory.BLOCKS, of(ItemTagGenerator.RED_LEAVES), new ItemStack(Items.NETHER_WART_BLOCK), 0.4f, 200);
        offerHaunting(output, "poisonous_potato", CookingBookCategory.FOOD, of(ConventionalItemTags.POTATO_CROPS), new ItemStack(Items.POISONOUS_POTATO), 0.2f, 200);
        offerHaunting(output, "prismarine_shard", CookingBookCategory.MISC, of(ConventionalItemTags.LAPIS_GEMS), new ItemStack(Items.PRISMARINE_SHARD), 0.7f, 200);
        offerHaunting(output, "raw_gold", CookingBookCategory.MISC, of(ConventionalItemTags.COPPER_RAW_MATERIALS), new ItemStack(Items.RAW_GOLD), 1.0f, 200);
        offerHaunting(output, "rotten_flesh", CookingBookCategory.FOOD, of(ConventionalItemTags.RAW_MEAT_FOODS), new ItemStack(Items.ROTTEN_FLESH), 0.3f, 200);
        offerHaunting(output, "shroom_light", CookingBookCategory.BLOCKS, of(Items.MUSHROOM_STEM, Items.RED_MUSHROOM_BLOCK, Items.BROWN_MUSHROOM_BLOCK), new ItemStack(Items.SHROOMLIGHT), 0.2f, 200);
        offerHaunting(output, "slime_ball", CookingBookCategory.MISC, of(ConventionalItemTags.BREAD_FOODS), new ItemStack(Items.SLIME_BALL), 0.3f, 200);
        offerHaunting(output, "soul_campfire", CookingBookCategory.BLOCKS, of(Items.CAMPFIRE), new ItemStack(Items.SOUL_CAMPFIRE), 0.4f, 200);
        offerHaunting(output, "soul_lantern", CookingBookCategory.BLOCKS, of(Items.LANTERN), new ItemStack(Items.SOUL_LANTERN), 0.4f, 200);
        offerHaunting(output, "soul_sand", CookingBookCategory.BLOCKS, of(ItemTags.SAND), new ItemStack(Items.SOUL_SAND), 0.4f, 200);
        offerHaunting(output, "soul_soil", CookingBookCategory.BLOCKS, of(ItemTags.DIRT), new ItemStack(Items.SOUL_SOIL), 0.4f, 200);
        offerHaunting(output, "soul_torch", CookingBookCategory.BLOCKS, of(Items.TORCH), new ItemStack(Items.SOUL_TORCH), 0.4f, 200);
        offerHaunting(output, "spectral_arrow", CookingBookCategory.MISC, of(Items.ARROW), new ItemStack(Items.SPECTRAL_ARROW), 0.5f, 200);
        offerHaunting(output, "tinted_glass", CookingBookCategory.BLOCKS, of(ConventionalItemTags.GLASS_BLOCKS_CHEAP), new ItemStack(Items.TINTED_GLASS), 0.6f, 200);
        offerHaunting(output, "warped_fungus", CookingBookCategory.MISC, of(Items.BROWN_MUSHROOM), new ItemStack(Items.WARPED_FUNGUS), 0.2f, 200);
        offerHaunting(output, "warped_stem", CookingBookCategory.BLOCKS, of(ItemTagGenerator.GREEN_WOODS), new ItemStack(Items.WARPED_STEM), 0.2f, 200);
        offerHaunting(output, "warped_wart_block", CookingBookCategory.BLOCKS, of(ItemTagGenerator.GREEN_LEAVES), new ItemStack(Items.WARPED_WART_BLOCK), 0.2f, 200);
        offerHaunting(output, "wither_rose", CookingBookCategory.MISC, of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.WITHER_ROSE), 0.1f, 200);
        offerHaunting(output, "wither_skeleton_skull", CookingBookCategory.BLOCKS, of(Items.SKELETON_SKULL), new ItemStack(Items.WITHER_SKELETON_SKULL), 0.8f, 200);
        offerHaunting(output, "blaze_rod", CookingBookCategory.MISC, of(Items.BREEZE_ROD, Items.END_ROD), new ItemStack(Items.BLAZE_ROD), 0.8f, 200);
        offerHaunting(output, "blaze_charge", CookingBookCategory.MISC, of(Items.WIND_CHARGE), new ItemStack(Items.FIRE_CHARGE), 0.2f, 200);
        offerHaunting(output, "lava_bucket", CookingBookCategory.MISC, of(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET), 0.2f, 200);
        
        // Corrupting recipes
        offerCorrupting(output, "chorus_fruit", CookingBookCategory.FOOD, of(ConventionalItemTags.FRUIT_FOODS), new ItemStack(Items.CHORUS_FRUIT), 0.4f, 200);
        offerCorrupting(output, "chorus_plant", CookingBookCategory.MISC, of(ItemTags.LOGS), new ItemStack(Items.CHORUS_PLANT), 0.2f, 200);
        offerCorrupting(output, "chorus_flower", CookingBookCategory.MISC, of(ItemTags.LEAVES), new ItemStack(Items.CHORUS_FLOWER), 0.2f, 200);
        offerCorrupting(output, "end_rod", CookingBookCategory.MISC, of(Items.BREEZE_ROD, Items.BLAZE_ROD), new ItemStack(Items.END_ROD), 0.8f, 200);
        offerCorrupting(output, "end_stone", CookingBookCategory.MISC, of(ConventionalItemTags.COBBLESTONES), new ItemStack(Items.END_STONE), 0.4f, 200);
        offerCorrupting(output, "end_stone_bricks", CookingBookCategory.BLOCKS, of(ItemTags.STONE_BRICKS), new ItemStack(Items.END_STONE_BRICKS), 0.4f, 200);
        offerCorrupting(output, "end_stone_brick_slab", CookingBookCategory.BLOCKS, of(Items.STONE_BRICK_SLAB), new ItemStack(Items.END_STONE_BRICK_SLAB), 0.4f, 200);
        offerCorrupting(output, "end_stone_brick_stairs", CookingBookCategory.BLOCKS, of(Items.STONE_BRICK_STAIRS), new ItemStack(Items.END_STONE_BRICK_STAIRS), 0.4f, 200);
        offerCorrupting(output, "end_stone_brick_wall", CookingBookCategory.BLOCKS, of(Items.STONE_BRICK_WALL), new ItemStack(Items.END_STONE_BRICK_WALL), 0.4f, 200);
        offerCorrupting(output, "dragon_breath", CookingBookCategory.MISC, of(Items.GLASS_BOTTLE), new ItemStack(Items.DRAGON_BREATH), 0.6f, 200);
        offerCorrupting(output, "ender_pearl", CookingBookCategory.MISC, of(ConventionalItemTags.PRISMARINE_GEMS), new ItemStack(Items.ENDER_PEARL), 0.6f, 200);
        offerCorrupting(output, "magenta_glass", CookingBookCategory.BLOCKS, of(ConventionalItemTags.GLASS_BLOCKS_CHEAP), new ItemStack(Items.MAGENTA_STAINED_GLASS), 0.2f, 200);
        offerCorrupting(output, "magenta_glass_pane", CookingBookCategory.BLOCKS, of(ConventionalItemTags.GLASS_PANES), new ItemStack(Items.MAGENTA_STAINED_GLASS_PANE), 0.2f, 100);
        offerCorrupting(output, "magenta_candle", CookingBookCategory.MISC, of(ItemTags.CANDLES), new ItemStack(Items.MAGENTA_CANDLE), 0.2f, 200);
        offerCorrupting(output, "dragon_head", CookingBookCategory.MISC, of(ConventionalItemTags.JACK_O_LANTERNS_PUMPKINS), new ItemStack(Items.DRAGON_HEAD), 1.0f, 1000);
        offerCorrupting(output, "shulker_box", CookingBookCategory.MISC, of(ConventionalItemTags.ENDER_CHESTS), new ItemStack(Items.SHULKER_BOX), 1.0f, 600);
    }

    public Ingredient of(TagKey<Item> tag) {
        return Ingredient.of(tag);
    }

    public Ingredient of(ItemLike... items) {
        return Ingredient.of(items);
    }

    public void offerHaunting(RecipeOutput output, String name, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        ResourceLocation id = HauntFurnace.id("haunting/" + name);
        HauntingRecipe hauntingRecipe = new HauntingRecipe("", category, input, result, experience, cookTime);
        Advancement.Builder criteria = output
            .advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(getHasName(ModBlocks.HAUNT_FURNACE.get().asItem()), has(ModBlocks.HAUNT_FURNACE.get().asItem()));
        output.accept(id, hauntingRecipe, criteria.build(id));
    }

    public void offerCorrupting(RecipeOutput output, String name, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        ResourceLocation id = HauntFurnace.id("corrupting/" + name);
        CorruptingRecipe corruptingRecipe = new CorruptingRecipe("", category, input, result, experience, cookTime);
        Advancement.Builder criteria = output
            .advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(getHasName(ModBlocks.ENDER_FURNACE.get().asItem()), has(ModBlocks.ENDER_FURNACE.get().asItem()));
        output.accept(id, corruptingRecipe, criteria.build(id));
    }
}
