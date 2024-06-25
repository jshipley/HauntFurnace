package com.jship;

import team.reborn.energy.api.base.SimpleEnergyStorage;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class PoweredHauntFurnaceBlockEntity extends LockableContainerBlockEntity implements RecipeUnlocker, RecipeInputProvider {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int ENERGY_STORAGE_PROPERTY_INDEX = 0;
    public static final int COOK_TIME_PROPERTY_INDEX = 1;
    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 2;
    public static final int PROPERTY_COUNT = 3;
    public static final int DEFAULT_COOK_TIME = 100;
    protected DefaultedList<ItemStack> inventory;
    public static final int ENERGY_CAPACITY = 1024;
    public static final int ENERGY_MAX_INSERT = 32;
    public static final int ENERGY_MAX_EXTRACT = 0;
    private static final int ENERGY_USAGE_PER_TICK = 20;

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_INSERT, ENERGY_MAX_EXTRACT) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    int cookTime;
    int cookTimeTotal;
    boolean isBurning;
    int unpoweredCount = 0;
    protected final PropertyDelegate propertyDelegate;
    private final Object2IntOpenHashMap<Identifier> recipesUsed;
    private final RecipeManager.MatchGetter<SingleStackRecipeInput, ? extends HauntingRecipe> matchGetter;

    protected PoweredHauntFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case ENERGY_STORAGE_PROPERTY_INDEX:
                        // energyStorage.amount is a long, and could possibly be much larger than what
                        // can be reported to the GUI in this PropertyDelegate
                        // This shouldn't be an issue, because the configured ENERGY_CAPACITY
                        // is also an int and it won't realistically be set anywhere near the limit of an integer
                        return (int) PoweredHauntFurnaceBlockEntity.this.energyStorage.amount;
                    case COOK_TIME_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.cookTime;
                    case COOK_TIME_TOTAL_PROPERTY_INDEX:
                        return PoweredHauntFurnaceBlockEntity.this.cookTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case ENERGY_STORAGE_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.energyStorage.amount = (long) value;
                        break;
                    case COOK_TIME_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookTime = value;
                        break;
                    case COOK_TIME_TOTAL_PROPERTY_INDEX:
                        PoweredHauntFurnaceBlockEntity.this.cookTimeTotal = value;
                }
            }

            public int size() {
                return PROPERTY_COUNT;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap<Identifier>();
        this.matchGetter = RecipeManager.createCachedMatchGetter(HauntFurnace.HAUNTING_RECIPE);
    }

    protected Text getContainerName() {
        return Text.translatable("container.powered_haunt_furnace");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new PoweredHauntFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        this.energyStorage.amount = nbt.getLong("EnergyStorage");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
        for (String string : nbtCompound.getKeys()) {
            this.recipesUsed.put(Identifier.of(string), nbtCompound.getInt(string));
        }
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putLong("EnergyStorage", (long) this.energyStorage.amount);
        nbt.putShort("CookTime", (short) this.cookTime);
        nbt.putShort("CookTimeTotal", (short) this.cookTimeTotal);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
        NbtCompound nbtCompound = new NbtCompound();
        this.recipesUsed.forEach((identifier, count) -> {
            nbtCompound.putInt(identifier.toString(), count);
        });
        nbt.put("RecipesUsed", nbtCompound);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PoweredHauntFurnaceBlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        ItemStack inputItems = (ItemStack) blockEntity.inventory.get(0);
        RecipeEntry recipeEntry = !inputItems.isEmpty() ?
                (RecipeEntry) blockEntity.matchGetter.getFirstMatch(new SingleStackRecipeInput(inputItems), world).orElse(null)
                : null;
        boolean hasEnergy = blockEntity.energyStorage.amount > ENERGY_USAGE_PER_TICK;
        int stackMax = blockEntity.getMaxCountPerStack();
        boolean canOutput = canAcceptRecipeOutput(world.getRegistryManager(),
                recipeEntry, blockEntity.inventory, stackMax);

        boolean isBurning = false;
        boolean stateChanged = false;

        if (hasEnergy && recipeEntry != null && canOutput) {
            isBurning = true;
            blockEntity.unpoweredCount = 0;
            blockEntity.energyStorage.amount -= ENERGY_USAGE_PER_TICK;
            blockEntity.cookTime += 2;
            if (blockEntity.cookTime >= blockEntity.cookTimeTotal) {
                blockEntity.cookTime = 0;
                blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
                if (craftRecipe(world.getRegistryManager(), recipeEntry, blockEntity.inventory, stackMax)) {
                    blockEntity.setLastRecipe(recipeEntry);
                }
            }
        } else if (recipeEntry != null && canOutput) {
            // we could craft if we had energy... lose progress on cooking
            blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        } else {
            // either nothing to cook, or nowhere to put it
            blockEntity.cookTime = 0;
        }

        if (!isBurning) {
            ++blockEntity.unpoweredCount;
            if (blockEntity.isBurning() && blockEntity.unpoweredCount > 4) {
                stateChanged = true;
                blockEntity.isBurning = isBurning;
            }
        } else if (!blockEntity.isBurning()) {
            stateChanged = true;
            blockEntity.isBurning = isBurning;
        }

        if (stateChanged) {
            state = (BlockState)state.with(PoweredHauntFurnaceBlock.LIT, blockEntity.isBurning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            markDirty(world, pos, state);
        }
    }

    private boolean isBurning() {
        return isBurning;
    }

    private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int stackMax) {
        if (!((ItemStack) slots.get(INPUT_SLOT)).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.value().getResult(registryManager);
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = (ItemStack) slots.get(OUTPUT_SLOT);
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsEqual(outputStack, recipeOutput)) {
                    return false;
                } else if (outputStack.getCount() < stackMax && outputStack.getCount() < outputStack.getMaxCount()) {
                    return true;
                } else {
                    return outputStack.getCount() < recipeOutput.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(registryManager, recipe, slots, count)) {
            ItemStack inputStack = (ItemStack) slots.get(INPUT_SLOT);
            ItemStack recipeOutput = recipe.value().getResult(registryManager);
            ItemStack outputStack = (ItemStack) slots.get(OUTPUT_SLOT);
            if (outputStack.isEmpty()) {
                slots.set(OUTPUT_SLOT, recipeOutput.copy());
            } else if (outputStack.isOf(recipeOutput.getItem())) {
                outputStack.increment(1);
            }

            inputStack.decrement(1);
            return true;
        } else {
            return false;
        }
    }

    private static int getCookTime(World world, PoweredHauntFurnaceBlockEntity furnace) {
        return (Integer) furnace.matchGetter.getFirstMatch(new SingleStackRecipeInput(furnace.getStack(0)), world).map(recipe -> ((AbstractCookingRecipe) recipe.value()).getCookingTime()).orElse(DEFAULT_COOK_TIME);
    }

    public int size() {
        return this.inventory.size();
    }

    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    public void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public boolean isEmpty() {
        Iterator inv = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!inv.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) inv.next();
        } while (itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return (ItemStack) this.inventory.get(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack) this.inventory.get(slot);
        boolean canCombine = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot == INPUT_SLOT && !canCombine) {
            this.cookTimeTotal = getCookTime(this.world, this);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
        return slot == OUTPUT_SLOT;
    }
    public void clear() {
        this.inventory.clear();
    }

    public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.id();
            this.recipesUsed.addTo(identifier, 1);
        }

    }

    @Nullable
    public RecipeEntry<?> getLastRecipe() {
        return null;
    }

    public void unlockLastRecipe(PlayerEntity player, List<ItemStack> ingredients) {
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        List<RecipeEntry<?>> list = this.getRecipesUsedAndDropExperience(player.getServerWorld(), player.getPos());
        player.unlockRecipes(list);
        for (RecipeEntry<?> recipe : list) {
            if (recipe != null) {
                player.onRecipeCrafted(recipe, this.inventory);
            }
        }

        this.recipesUsed.clear();
    }

    public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
        List<RecipeEntry<?>> list = Lists.newArrayList();
        for (Object2IntMap.Entry entry : this.recipesUsed.object2IntEntrySet()) {
            world.getRecipeManager().get((Identifier) entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe) recipe.value()).getExperience());
            });
        }

        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float) multiplier * experience);
        float f = MathHelper.fractionalPart((float) multiplier * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        ExperienceOrbEntity.spawn(world, pos, i);
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addInput(itemStack);
        }
    }
}
