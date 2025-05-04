package com.jship.hauntfurnace.block.entity;

import com.jship.hauntfurnace.HauntFurnace;

import org.jetbrains.annotations.Nullable;

import com.jship.hauntfurnace.recipe.CorruptingRecipe;
import com.jship.spiritapi.SpiritAPI;
import com.jship.spiritapi.api.energy.SpiritEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

// This needs to use FE instead of fuel, so it needed to change a lot from the AbstractFurnaceBlockEntity.
public abstract class AbstractPoweredFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    // Only adding variables that are different from AbstractFurnaceBlockEntity
    // Even though this won't use the FUEL slot, other mods (like Jade) might expect
    // to be able to access the input/output slots by index
    public static final int NUM_SLOTS = 3;
    public static final int[] SLOTS_FOR_DOWN = new int[] { SLOT_RESULT };
    public static final int[] SLOTS_FOR_SIDES = new int[] { SLOT_INPUT };
    public static final int DATA_ENERGY_STORAGE = 4;
    public static final int NUM_DATA_VALUES = 5;
    private static final int ENERGY_USAGE_PER_TICK = 10;
    public static final int ENERGY_CAPACITY = 1024;
    public static final int ENERGY_MAX_INSERT = 32;
    public static final int ENERGY_MAX_EXTRACT = ENERGY_USAGE_PER_TICK;

    protected final ContainerData poweredDataAccess;
    public final SpiritEnergyStorage energyStorage;

    public AbstractPoweredFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(blockEntityType, pos, state, recipeType);
        this.items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
        this.energyStorage = SpiritEnergyStorage.create(ENERGY_CAPACITY, ENERGY_MAX_INSERT, ENERGY_MAX_EXTRACT, () -> this.setChanged());
        this.poweredDataAccess = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case DATA_LIT_TIME:
                        return AbstractPoweredFurnaceBlockEntity.this.litTimeRemaining;
                    case DATA_LIT_DURATION:
                        return AbstractPoweredFurnaceBlockEntity.this.litTotalTime;
                    case DATA_COOKING_PROGRESS:
                        return AbstractPoweredFurnaceBlockEntity.this.cookingTimer;
                    case DATA_COOKING_TOTAL_TIME:
                        return AbstractPoweredFurnaceBlockEntity.this.cookingTotalTime;
                    case DATA_ENERGY_STORAGE:
                        return (int)AbstractPoweredFurnaceBlockEntity.this.energyStorage.getEnergyStored();
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case DATA_LIT_TIME:
                        AbstractPoweredFurnaceBlockEntity.this.litTimeRemaining = value;
                        break;
                    case DATA_LIT_DURATION:
                        AbstractPoweredFurnaceBlockEntity.this.litTotalTime = value;
                        break;
                    case DATA_COOKING_PROGRESS:
                        AbstractPoweredFurnaceBlockEntity.this.cookingTimer = value;
                        break;
                    case DATA_COOKING_TOTAL_TIME:
                        AbstractPoweredFurnaceBlockEntity.this.cookingTotalTime = value;
                        break;
                    case DATA_ENERGY_STORAGE:
                        AbstractPoweredFurnaceBlockEntity.this.energyStorage.setEnergyStored(value);
                        break;
                }
            }

            public int getCount() {
                return NUM_DATA_VALUES;
            }
        };
    }

    protected boolean isLit() {
        return litTimeRemaining > 0;
    }

    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.energyStorage.setEnergyStored(compoundTag.getIntOr("EnergyStorage", 0));
    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        compoundTag.putInt("EnergyStorage", (short) this.energyStorage.getEnergyStored());
    }

    // Update cooking progress and block state
    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, AbstractPoweredFurnaceBlockEntity blockEntity) {
        boolean wasLit = blockEntity.isLit();
        boolean markDirty = false;

        if (blockEntity.isLit()) {
            blockEntity.litTimeRemaining--;
        }

        // Slots
        ItemStack inputItems = (ItemStack) blockEntity.getItem(SLOT_INPUT);
        ItemStack outputItems = (ItemStack) blockEntity.getItem(SLOT_RESULT);

        SingleRecipeInput recipeInput = new SingleRecipeInput(inputItems);

        // Recipe and output
        @Nullable
        RecipeHolder<?> recipe = !inputItems.isEmpty() ? (RecipeHolder<?>) blockEntity.quickCheck.getRecipeFor(recipeInput, (ServerLevel) level).orElse(null) : null;
        ItemStack recipeOutput = recipe != null ? ((CorruptingRecipe) recipe.value()).assemble(recipeInput, level.registryAccess()) : ItemStack.EMPTY;

        // There's a recipe that has output for the furnace input, and the output can
        // fit in the output slot
        boolean canOutput = !recipeOutput.isEmpty() && (outputItems.isEmpty() || (ItemStack.isSameItem(recipeOutput, outputItems) && outputItems.getCount() < blockEntity.getMaxStackSize() && outputItems.getCount() < outputItems.getMaxStackSize()));

        if (canOutput && (blockEntity.energyStorage.extractEnergy(ENERGY_USAGE_PER_TICK, true) == ENERGY_USAGE_PER_TICK)) {
            markDirty = true;
            blockEntity.energyStorage.extractEnergy(ENERGY_USAGE_PER_TICK, false);
            blockEntity.litTimeRemaining = 4;
            blockEntity.cookingTimer += 2;
            if (blockEntity.cookingTimer >= blockEntity.cookingTotalTime) {
                blockEntity.cookingTimer = 0;
                blockEntity.cookingTotalTime = getTotalCookTime((ServerLevel) level, blockEntity);
                inputItems.shrink(1);
                if (outputItems.isEmpty()) {
                    blockEntity.items.set(SLOT_RESULT, recipeOutput.copy());
                } else {
                    outputItems.grow(1);
                }
                blockEntity.setRecipeUsed(recipe);
            }
        } else if (canOutput) {
            // we could craft if we had energy... lose progress on cooking
            blockEntity.cookingTimer = Mth.clamp(blockEntity.cookingTimer - 2, 0, blockEntity.cookingTotalTime);
        } else {
            // either nothing to cook, or nowhere to put it
            blockEntity.cookingTimer = 0;
        }

        if (blockEntity.isLit() != wasLit) {
            markDirty = true;
            blockState = (BlockState) blockState.setValue(AbstractFurnaceBlock.LIT, blockEntity.isLit());    
            level.setBlockAndUpdate(blockPos, blockState);
        }

        if (markDirty)
            setChanged(level, blockPos, blockState);
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(itemStack -> itemStack.isEmpty());
    }

    public ItemStack getItem(int i) {
        return (ItemStack) this.items.get(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        ItemStack itemStack2 = this.getItem(i);
        boolean sameItemAdded = !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack2, itemStack);
        this.items.set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (i == SLOT_INPUT && !sameItemAdded) {
            this.cookingTotalTime = getTotalCookTime((ServerLevel) this.level, this);
            this.cookingTimer = 0;
            this.setChanged();
        }
    }

    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return i == SLOT_INPUT;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return i == SLOT_RESULT;
    }

    @Nullable
    public SpiritEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return energyStorage;
    }
}
