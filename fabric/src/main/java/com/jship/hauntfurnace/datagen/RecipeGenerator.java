package com.jship.hauntfurnace.datagen;

import java.util.concurrent.CompletableFuture;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HauntFurnace.HAUNT_FURNACE_ITEM.get())
                .pattern("BBB").pattern("BSB").pattern("BBB")
                .define('B', Ingredient.of(Items.BLACKSTONE))
                .define('S', Ingredient.of(ItemTags.SOUL_FIRE_BASE_BLOCKS))
                .unlockedBy("has_soul_fire", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
                .unlockedBy(getHasName(Items.BLACKSTONE), has(Items.BLACKSTONE))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HauntFurnace.POWERED_HAUNT_FURNACE_ITEM.get())
                .pattern("GGG").pattern("GHG").pattern("GRG")
                .define('G', Ingredient.of(Items.BLACKSTONE))
                .define('H', Ingredient.of(HauntFurnace.HAUNT_FURNACE_ITEM.get()))
                .define('R', ConventionalItemTags.REDSTONE_DUSTS)
                .unlockedBy(getHasName(HauntFurnace.HAUNT_FURNACE_ITEM.get()), has(HauntFurnace.HAUNT_FURNACE_ITEM.get()))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.GILDED_BLACKSTONE)
                .pattern("NNN").pattern("NBN").pattern("NNN")
                .define('N', Ingredient.of(ConventionalItemTags.GOLD_NUGGETS))
                .define('B', Ingredient.of(Items.BLACKSTONE))
                .unlockedBy(getHasName(Items.BLACKSTONE), has(Items.BLACKSTONE))
                .save(exporter);
        
        offerHaunting(exporter, "blackstone", CookingBookCategory.BLOCKS, Ingredient.of(ConventionalItemTags.COBBLESTONES), new ItemStack(Items.BLACKSTONE), 0.1f, 200);
        offerHaunting(exporter, "crimson_fungus", CookingBookCategory.MISC, Ingredient.of(Items.RED_MUSHROOM), new ItemStack(Items.CRIMSON_FUNGUS), 0.2f, 200);
        offerHaunting(exporter, "crimson_stem", CookingBookCategory.MISC, Ingredient.of(ItemTagGenerator.RED_WOODS), new ItemStack(Items.CRIMSON_STEM), 0.2f, 200);
        offerHaunting(exporter, "crying_obsidian", CookingBookCategory.BLOCKS, Ingredient.of(ConventionalItemTags.NORMAL_OBSIDIANS), new ItemStack(Items.CRYING_OBSIDIAN), 0.4f, 200);
        offerHaunting(exporter, "dead_bush", CookingBookCategory.MISC, Ingredient.of(ItemTags.SAPLINGS), new ItemStack(Items.DEAD_BUSH), 0.2f, 200);
        offerHaunting(exporter, "ender_eye", CookingBookCategory.MISC, Ingredient.of(Items.ENDER_PEARL), new ItemStack(Items.ENDER_EYE), 0.4f, 200);
        offerHaunting(exporter, "gold_ingot", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.COPPER_INGOTS), new ItemStack(Items.GOLD_INGOT), 1.0f, 200);
        offerHaunting(exporter, "glow_berries", CookingBookCategory.FOOD, Ingredient.of(ConventionalItemTags.BERRY_FOODS), new ItemStack(Items.GLOW_BERRIES), 0.2f, 200);
        offerHaunting(exporter, "glow_ink_sac", CookingBookCategory.MISC, Ingredient.of(Items.INK_SAC), new ItemStack(Items.GLOW_INK_SAC), 0.3f, 200);
        offerHaunting(exporter, "glowstone", CookingBookCategory.MISC, Ingredient.of(Items.REDSTONE), new ItemStack(Items.GLOWSTONE), 0.7f, 200);
        offerHaunting(exporter, "glowstone_dust", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.REDSTONE_DUSTS), new ItemStack(Items.GLOWSTONE_DUST), 0.4f, 200);
        offerHaunting(exporter, "infested_chiseled_stone_bricks", CookingBookCategory.BLOCKS, Ingredient.of(Items.CHISELED_STONE_BRICKS), new ItemStack(Items.INFESTED_CHISELED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(exporter, "infested_cracked_stone_bricks", CookingBookCategory.BLOCKS, Ingredient.of(Items.CRACKED_STONE_BRICKS), new ItemStack(Items.INFESTED_CRACKED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(exporter, "infested_deepslate", CookingBookCategory.BLOCKS, Ingredient.of(Items.DEEPSLATE), new ItemStack(Items.INFESTED_DEEPSLATE), 0.1f, 200);
        offerHaunting(exporter, "infested_mossy_stone_bricks", CookingBookCategory.BLOCKS, Ingredient.of(Items.MOSSY_STONE_BRICKS), new ItemStack(Items.INFESTED_MOSSY_STONE_BRICKS), 0.1f, 200);
        offerHaunting(exporter, "infested_stone", CookingBookCategory.BLOCKS, Ingredient.of(Items.STONE), new ItemStack(Items.INFESTED_STONE), 0.1f, 200);
        offerHaunting(exporter, "infested_stone_bricks", CookingBookCategory.BLOCKS, Ingredient.of(Items.STONE_BRICKS), new ItemStack(Items.INFESTED_STONE_BRICKS), 0.1f, 200);
        offerHaunting(exporter, "nether_brick", CookingBookCategory.MISC, Ingredient.of(Items.BRICK), new ItemStack(Items.NETHER_BRICK), 0.1f, 200);
        offerHaunting(exporter, "nether_bricks", CookingBookCategory.BLOCKS, Ingredient.of(Items.BRICKS), new ItemStack(Items.NETHER_BRICKS), 0.1f, 200);
        offerHaunting(exporter, "nether_wart", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.SEEDS), new ItemStack(Items.NETHER_WART), 0.2f, 200);
        offerHaunting(exporter, "nether_wart_block", CookingBookCategory.BLOCKS, Ingredient.of(ItemTagGenerator.RED_LEAVES), new ItemStack(Items.NETHER_WART_BLOCK), 0.4f, 200);
        offerHaunting(exporter, "poisonous_potato", CookingBookCategory.FOOD, Ingredient.of(ConventionalItemTags.POTATO_CROPS), new ItemStack(Items.POISONOUS_POTATO), 0.2f, 200);
        offerHaunting(exporter, "prismarine_shard", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.LAPIS_GEMS), new ItemStack(Items.PRISMARINE_SHARD), 0.7f, 200);
        offerHaunting(exporter, "raw_gold", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.COPPER_RAW_MATERIALS), new ItemStack(Items.RAW_GOLD), 1.0f, 200);
        offerHaunting(exporter, "rotten_flesh", CookingBookCategory.FOOD, Ingredient.of(ConventionalItemTags.RAW_MEAT_FOODS), new ItemStack(Items.ROTTEN_FLESH), 0.3f, 200);
        offerHaunting(exporter, "shroom_light", CookingBookCategory.BLOCKS, Ingredient.of(Items.MUSHROOM_STEM, Items.RED_MUSHROOM_BLOCK, Items.BROWN_MUSHROOM_BLOCK), new ItemStack(Items.SHROOMLIGHT), 0.2f, 200);
        offerHaunting(exporter, "slime_ball", CookingBookCategory.MISC, Ingredient.of(ConventionalItemTags.BREAD_FOODS), new ItemStack(Items.SLIME_BALL), 0.3f, 200);
        offerHaunting(exporter, "soul_campfire", CookingBookCategory.BLOCKS, Ingredient.of(Items.CAMPFIRE), new ItemStack(Items.SOUL_CAMPFIRE), 0.4f, 200);
        offerHaunting(exporter, "soul_lantern", CookingBookCategory.BLOCKS, Ingredient.of(Items.LANTERN), new ItemStack(Items.SOUL_LANTERN), 0.4f, 200);
        offerHaunting(exporter, "soul_sand", CookingBookCategory.BLOCKS, Ingredient.of(ItemTags.SAND), new ItemStack(Items.SOUL_SAND), 0.4f, 200);
        offerHaunting(exporter, "soul_soil", CookingBookCategory.BLOCKS, Ingredient.of(ItemTags.DIRT), new ItemStack(Items.SOUL_SOIL), 0.4f, 200);
        offerHaunting(exporter, "soul_torch", CookingBookCategory.BLOCKS, Ingredient.of(Items.TORCH), new ItemStack(Items.SOUL_TORCH), 0.4f, 200);
        offerHaunting(exporter, "spectral_arrow", CookingBookCategory.MISC, Ingredient.of(Items.ARROW), new ItemStack(Items.SPECTRAL_ARROW), 0.5f, 200);
        offerHaunting(exporter, "tinted_glass", CookingBookCategory.BLOCKS, Ingredient.of(ConventionalItemTags.GLASS_BLOCKS_CHEAP), new ItemStack(Items.TINTED_GLASS), 0.6f, 200);
        offerHaunting(exporter, "warped_fungus", CookingBookCategory.MISC, Ingredient.of(Items.BROWN_MUSHROOM), new ItemStack(Items.WARPED_FUNGUS), 0.2f, 200);
        offerHaunting(exporter, "warped_stem", CookingBookCategory.BLOCKS, Ingredient.of(ItemTagGenerator.GREEN_WOODS), new ItemStack(Items.WARPED_STEM), 0.2f, 200);
        offerHaunting(exporter, "warped_wart_block", CookingBookCategory.BLOCKS, Ingredient.of(ItemTagGenerator.GREEN_LEAVES), new ItemStack(Items.WARPED_WART_BLOCK), 0.2f, 200);
        offerHaunting(exporter, "wither_rose", CookingBookCategory.MISC, Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.WITHER_ROSE), 0.1f, 200);
        offerHaunting(exporter, "wither_skeleton_skull", CookingBookCategory.BLOCKS, Ingredient.of(Items.SKELETON_SKULL), new ItemStack(Items.WITHER_SKELETON_SKULL), 0.8f, 200);
    }

    public static void offerHaunting(RecipeOutput exporter, String name, CookingBookCategory category, Ingredient input, ItemStack result, float experience, int cookTime) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HauntFurnace.MOD_ID, "haunting/" + name);
        HauntingRecipe hauntingRecipe = new HauntingRecipe("", category, input, result, experience, cookTime);
        Advancement.Builder criteria = exporter.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(getHasName(HauntFurnace.HAUNT_FURNACE_ITEM.get()), has(HauntFurnace.HAUNT_FURNACE_ITEM.get()));
        exporter.accept(id, hauntingRecipe, criteria.build(id.withPrefix("recipes/")));
    }
    
}
