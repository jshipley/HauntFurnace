package com.jship.hauntfurnace.block.entity;

import com.google.common.collect.Lists;
import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.PoweredHauntFurnaceBlock;
import com.jship.hauntfurnace.energy.EnergyStorageWrapper;
import com.jship.hauntfurnace.menu.PoweredHauntFurnaceMenu;
import com.jship.hauntfurnace.recipe.HauntingRecipe;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// This needs to use FE instead of fuel, so it needed to change a lot from the AbstractFurnaceBlockEntity.
// The code for this is mostly based on the AbstractFurnaceBlockEntity.
public class PoweredHauntFurnaceBlockEntity
    extends BlockEntity
    implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible, MenuProvider {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int[] SLOTS_FOR_UP = new int[] { INPUT_SLOT };
    public static final int[] SLOTS_FOR_DOWN = new int[] { OUTPUT_SLOT };
    public static final int[] SLOTS_FOR_SIDES = new int[] { INPUT_SLOT };
    public static final int ENERGY_STORAGE_PROPERTY_INDEX = 0;
    public static final int HAUNT_TIME_PROPERTY_INDEX = 1;
    public static final int HAUNT_TIME_TOTAL_PROPERTY_INDEX = 2;
    public static final int PROPERTY_COUNT = 3;
    public static final int DEFAULT_HAUNT_TIME = 100;
    private static final int ENERGY_USAGE_PER_TICK = 10;
    protected NonNullList<ItemStack> items;
    public static final int ENERGY_CAPACITY = 1024;
    public static final int ENERGY_MAX_INSERT = 32;
    public static final int ENERGY_MAX_EXTRACT = ENERGY_USAGE_PER_TICK;
    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);

    // This is a custom interface that should be implemented in the Forge/Fabric
    // specific code.
    public final EnergyStorageWrapper energyStorage;

    private int cookingProgress;
    private int cookingTotalTime;
    private boolean isLit;
    private int unpoweredCount = 0;
    private LockCode lockKey;

    protected final ContainerData dataAccess;
    private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed;
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;

    public PoweredHauntFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.BlockEntities.POWERED_HAUNT_FURNACE.get(), pos, state);
        this.lockKey = LockCode.NO_LOCK;
        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        this.energyStorage = HauntFurnace.ENERGY_STORAGE_FACTORY.get()
            .createEnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_INSERT, ENERGY_MAX_EXTRACT, this);
        this.dataAccess = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case ENERGY_STORAGE_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.energyStorage.getEnergyStored();
                    case HAUNT_TIME_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.cookingProgress;
                    case HAUNT_TIME_TOTAL_PROPERTY_INDEX:
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
                    case HAUNT_TIME_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookingProgress = value;
                        break;
                    case HAUNT_TIME_TOTAL_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookingTotalTime = value;
                        break;
                }
            }

            public int getCount() {
                return PROPERTY_COUNT;
            }
        };
        this.recipesUsed = new Reference2IntOpenHashMap<ResourceKey<Recipe<?>>>();
        this.quickCheck = RecipeManager.createCheck(HauntFurnace.Recipes.HAUNTING.get());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.powered_haunt_furnace");
    }

    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PoweredHauntFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    private boolean isLit() {
        return isLit;
    }

    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, registries);
        this.energyStorage.setEnergyStored(compoundTag.getIntOr("EnergyStorage", 0));
        this.cookingProgress = compoundTag.getShortOr("CookTime", (short) 0);
        this.cookingTotalTime = compoundTag.getShortOr("CookTimeTotal", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll((Map) compoundTag.read("recipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        compoundTag.putInt("EnergyStorage", (short) this.energyStorage.getEnergyStored());
        compoundTag.putShort("CookTime", (short) this.cookingProgress);
        compoundTag.putShort("CookTimeTotal", (short) this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.items, registries);
        compoundTag.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
    }

    // Update cooking progress and block state
    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, PoweredHauntFurnaceBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        // Slots
        ItemStack inputItems = (ItemStack) blockEntity.getItem(INPUT_SLOT);
        ItemStack outputItems = (ItemStack) blockEntity.getItem(OUTPUT_SLOT);

        SingleRecipeInput recipeInput = new SingleRecipeInput(inputItems);

        // Recipe and output
        @Nullable
        RecipeHolder<?> recipe = !inputItems.isEmpty()
            ? (RecipeHolder<?>) blockEntity.quickCheck.getRecipeFor(recipeInput, (ServerLevel) level).orElse(null)
            : null;
        ItemStack recipeOutput = recipe != null
            ? ((HauntingRecipe) recipe.value()).assemble(recipeInput, level.registryAccess())
            : ItemStack.EMPTY;

        // There's a recipe that has output for the furnace input, and the output can
        // fit in the output slot
        boolean canOutput =
            !recipeOutput.isEmpty() &&
            (outputItems.isEmpty() ||
                (ItemStack.isSameItem(recipeOutput, outputItems) &&
                    outputItems.getCount() < blockEntity.getMaxStackSize() &&
                    outputItems.getCount() < outputItems.getMaxStackSize()));

        boolean isLit = false;
        boolean stateChanged = false;

        if (canOutput && (blockEntity.energyStorage.extractEnergy(ENERGY_USAGE_PER_TICK, false) == ENERGY_USAGE_PER_TICK)) {
            isLit = true;
            blockEntity.unpoweredCount = 0;
            blockEntity.cookingProgress += 2;
            if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
                blockEntity.cookingProgress = 0;
                blockEntity.cookingTotalTime = getTotalHauntTime((ServerLevel) level, blockEntity);
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

    private static int getTotalHauntTime(ServerLevel level, PoweredHauntFurnaceBlockEntity blockEntity) {
        return (Integer) blockEntity.quickCheck
            .getRecipeFor(new SingleRecipeInput(blockEntity.getItem(INPUT_SLOT)), level)
            .map(recipe -> ((HauntingRecipe) recipe.value()).cookingTime())
            .orElse(DEFAULT_HAUNT_TIME);
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public NonNullList<ItemStack> getHeldStacks() {
        return this.items;
    }

    public void setHeldStacks(NonNullList<ItemStack> items) {
        this.items = items;
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
        boolean sameItemAdded = !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack2, itemStack);
        this.items.set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (i == INPUT_SLOT && !sameItemAdded) {
            this.cookingTotalTime = getTotalHauntTime((ServerLevel) this.level, this);
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

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(i, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN && i == OUTPUT_SLOT;
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            ResourceKey<Recipe<?>> resourceKey = recipe.id();
            this.recipesUsed.addTo(resourceKey, 1);
        }
    }

    @Nullable
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player player, List<ItemStack> list) {}

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
        player.awardRecipes(list);

        for (RecipeHolder<?> recipeHolder : list) {
            if (recipeHolder != null) {
                player.triggerRecipeCrafted(recipeHolder, this.items);
            }
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
        List<RecipeHolder<?>> recipes = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            serverLevel
                .recipeAccess()
                .byKey((ResourceKey<Recipe<?>>) entry.getKey())
                .ifPresent(recipe -> {
                    recipes.add(recipe);
                    createExperience(serverLevel, vec3, entry.getIntValue(), ((HauntingRecipe) recipe.value()).experience());
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

    public void fillStackedContents(StackedItemContents stackedItemContents) {
        Iterator<ItemStack> iter = this.items.iterator();

        while (iter.hasNext()) {
            ItemStack itemStack = (ItemStack) iter.next();
            stackedItemContents.accountStack(itemStack);
        }
    }

    @Nullable
    public EnergyStorageWrapper getEnergyStorage(@Nullable Direction side) {
        return energyStorage;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PoweredHauntFurnaceMenu(containerId, playerInventory, this, this.dataAccess);
    }
}
