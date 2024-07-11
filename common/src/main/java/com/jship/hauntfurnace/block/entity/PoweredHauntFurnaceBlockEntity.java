package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.energy.EnergyStorage;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

// This needs to use FE instead of fuel, so it needed to change a lot from the AbstractFurnaceBlockEntity.
// The code for this is mostly based on the AbstractFurnaceBlockEntity.
public class PoweredHauntFurnaceBlockEntity extends BaseContainerBlockEntity
        implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int[] SLOTS_FOR_UP = new int[] { INPUT_SLOT };
    public static final int[] SLOTS_FOR_DOWN = new int[] { OUTPUT_SLOT };
    public static final int[] SLOTS_FOR_SIDES = new int[] {};
    public static final int ENERGY_STORAGE_PROPERTY_INDEX = 0;
    public static final int COOK_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 2;
    public static final int PROPERTY_COUNT = 3;
    public static final int DEFAULT_COOK_TIME = 100;
    protected NonNullList<ItemStack> items;
    public static final int ENERGY_CAPACITY = 1024;
    public static final int ENERGY_MAX_INSERT = 32;
    public static final int ENERGY_MAX_EXTRACT = 0;
    private static final int ENERGY_USAGE_PER_TICK = 10;

    // This is a custom interface that should be implemented in the Forge/Fabric
    // specific code.
    public final EnergyStorage energyStorage;

    int cookingProgress;
    int cookingTotalTime;
    boolean isLit;
    int unpoweredCount = 0;

    protected final ContainerData dataAccess;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public PoweredHauntFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY, pos, state);
        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        this.energyStorage = HauntFurnace.ENERGY_STORAGE_FACTORY.createEnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_INSERT,
                ENERGY_MAX_EXTRACT, this);
        this.dataAccess = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case ENERGY_STORAGE_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.energyStorage.getEnergyStored();
                    case COOK_TIME_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.cookingProgress;
                    case COOK_TIME_TOTAL_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.cookingTotalTime;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case ENERGY_STORAGE_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.energyStorage.setEnergyStored(value);
                        break;
                    case COOK_TIME_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookingProgress = value;
                        break;
                    case COOK_TIME_TOTAL_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookingTotalTime = value;
                        break;
                }
            }

            public int getCount() {
                return PROPERTY_COUNT;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap<ResourceLocation>();
        this.quickCheck = RecipeManager.createCheck(HauntFurnace.HAUNTING_RECIPE);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.powered_haunt_furnace");
    }

    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PoweredHauntFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    private boolean isLit() {
        return isLit;
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.energyStorage.setEnergyStored(compoundTag.getInt("EnergyStorage"));
        this.cookingProgress = compoundTag.getShort("CookTime");
        this.cookingTotalTime = compoundTag.getShort("CookTimeTotal");
        CompoundTag recipesCompoundTag = compoundTag.getCompound("RecipesUsed");
        recipesCompoundTag.getAllKeys().forEach(
                recipe -> this.recipesUsed.put(new ResourceLocation(recipe), recipesCompoundTag.getInt(recipe)));
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("EnergyStorage", (short) this.energyStorage.getEnergyStored());
        compoundTag.putShort("CookTime", (short) this.cookingProgress);
        compoundTag.putShort("CookTimeTotal", (short) this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        CompoundTag recipesCompoundTag = new CompoundTag();
        this.recipesUsed.forEach((resourceLocation, integer) -> {
            recipesCompoundTag.putInt(resourceLocation.toString(), integer);
        });
        compoundTag.put("RecipesUsed", recipesCompoundTag);
    }

    // Update cooking progress and block state
    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
            PoweredHauntFurnaceBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        // Slots
        ItemStack inputItems = (ItemStack) blockEntity.items.get(INPUT_SLOT);
        ItemStack outputItems = (ItemStack) blockEntity.items.get(OUTPUT_SLOT);

        // Recipe and output
        @Nullable
        Recipe<?> recipe = !inputItems.isEmpty()
                ? (Recipe<?>) blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null)
                : null;
        ItemStack recipeOutput = recipe != null ? recipe.getResultItem(level.registryAccess()) : ItemStack.EMPTY;

        // There's a recipe that has output for the furnace input, and the output can
        // fit in the output slot
        boolean canOutput = !recipeOutput.isEmpty()
                && (outputItems.isEmpty()
                        || (ItemStack.isSameItem(recipeOutput, outputItems)
                                && outputItems.getCount() < blockEntity.getMaxStackSize()
                                && outputItems.getCount() < outputItems.getMaxStackSize()));

        boolean isLit = false;
        boolean stateChanged = false;

        if (canOutput
                && (blockEntity.energyStorage.consumeEnergy(ENERGY_USAGE_PER_TICK, false) == ENERGY_USAGE_PER_TICK)) {
            isLit = true;
            blockEntity.unpoweredCount = 0;
            blockEntity.cookingProgress += 2;
            if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
                blockEntity.cookingProgress = 0;
                blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
                inputItems.shrink(1);
                if (outputItems.isEmpty()) {
                    blockEntity.items.set(OUTPUT_SLOT, recipeOutput.copy());
                } else {
                    outputItems.grow(1);
                }
                blockEntity.setRecipeUsed(recipe);
            }
        } else if (canOutput) {
            // we could craft if we had energy... lose progress on cooking
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        } else {
            // either nothing to cook, or nowhere to put it
            blockEntity.cookingProgress = 0;
        }

        if (!isLit) {
            ++blockEntity.unpoweredCount;
            // delay changing the LIT state to false to avoid excessive flickering at low
            // power
            if (blockEntity.isLit() && blockEntity.unpoweredCount > 4) {
                stateChanged = true;
                blockEntity.isLit = isLit;
            }
        } else if (!blockEntity.isLit()) {
            stateChanged = true;
            blockEntity.isLit = isLit;
        }

        if (stateChanged) {
            blockState = (BlockState) blockState.setValue(PoweredHauntFurnaceBlock.LIT, blockEntity.isLit());
            level.setBlockAndUpdate(blockPos, blockState);
            setChanged(level, blockPos, blockState);
        }
    }

    private static int getTotalCookTime(Level level, PoweredHauntFurnaceBlockEntity blockEntity) {
        return (Integer) blockEntity.quickCheck.getRecipeFor(blockEntity, level)
                .map(AbstractCookingRecipe::getCookingTime)
                .orElse(DEFAULT_COOK_TIME);
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(itemStack -> itemStack.isEmpty());
    }

    public ItemStack getItem(int i) {
        return (ItemStack) this.items.get(i);
    }

    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(this.items, i, j);
    }

    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    public void setItem(int i, ItemStack itemStack) {
        ItemStack itemStack2 = this.getItem(i);
        boolean sameItemAdded = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack2, itemStack);
        this.items.set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (i == INPUT_SLOT && !sameItemAdded) {
            this.cookingTotalTime = getTotalCookTime(this.level, this);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return i == INPUT_SLOT;
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(i, itemStack);
    }

    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN && i == OUTPUT_SLOT;
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourceLocation = recipe.getId();
            this.recipesUsed.addTo(resourceLocation, 1);
        }
    }

    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player player, List<ItemStack> list) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer) {
        List<Recipe<?>> recipes = this.getRecipesToAwardAndPopExperience(serverPlayer.serverLevel(),
                serverPlayer.position());
        serverPlayer.awardRecipes(recipes);
        for (Recipe<?> recipe : recipes) {
            if (recipe != null) {
                serverPlayer.triggerRecipeCrafted(recipe, this.items);
            }
        }

        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
        List<Recipe<?>> recipes = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            serverLevel.getRecipeManager().byKey((ResourceLocation) entry.getKey()).ifPresent((recipe) -> {
                recipes.add(recipe);
                createExperience(serverLevel, vec3, entry.getIntValue(),
                        ((HauntingRecipe) recipe).getExperience());
            });
        }

        return recipes;
    }

    private static void createExperience(ServerLevel serverLevel, Vec3 vec3, int i, float f) {
        int j = Mth.floor((float) i * f);
        float g = Mth.frac((float) i * f);
        if (g != 0.0F && Math.random() < (double) g) {
            ++j;
        }

        ExperienceOrb.award(serverLevel, vec3, j);
    }

    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack itemStack : this.items) {
            stackedContents.accountStack(itemStack);
        }
    }
}
