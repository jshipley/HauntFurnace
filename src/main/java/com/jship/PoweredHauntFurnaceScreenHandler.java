package com.jship;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class PoweredHauntFurnaceScreenHandler extends AbstractRecipeScreenHandler<SingleStackRecipeInput, AbstractCookingRecipe> {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int CRAFT_SLOTS = 2;
    public static final int INVENTORY_SLOTS = 29;
    public static final int TOTAL_SLOTS = 38;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookCategory category;
    public PoweredHauntFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(CRAFT_SLOTS), new ArrayPropertyDelegate(3));
    }

    public PoweredHauntFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(HauntFurnace.POWERED_HAUNT_FURNACE_SCREEN_HANDLER, syncId);
        this.recipeType = HauntFurnace.HAUNTING_RECIPE;
        this.category = RecipeBookCategory.FURNACE;
        checkSize(inventory, CRAFT_SLOTS);
        checkDataCount(propertyDelegate, 3);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.getWorld();
        this.addSlot(new Slot(inventory, INPUT_SLOT, 56, 35));
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, OUTPUT_SLOT, 116, 35));

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperties(propertyDelegate);
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
        }

    }

    public void clearCraftingSlots() {
        this.getSlot(INPUT_SLOT).setStackNoCallbacks(ItemStack.EMPTY);
        this.getSlot(OUTPUT_SLOT).setStackNoCallbacks(ItemStack.EMPTY);
    }

    public boolean matches(RecipeEntry<AbstractCookingRecipe> recipe) {
        return recipe.value().matches(new SingleStackRecipeInput(this.inventory.getStack(0)), this.world);
    }

    public int getCraftingResultSlotIndex() {
        return OUTPUT_SLOT;
    }

    public int getCraftingWidth() {
        return 1;
    }

    public int getCraftingHeight() {
        return 1;
    }

    public int getCraftingSlotCount() {
        return CRAFT_SLOTS;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == OUTPUT_SLOT) {
                if (!this.insertItem(itemStack2, CRAFT_SLOTS, TOTAL_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot != INPUT_SLOT) {
                if (this.isHauntable(itemStack2)) {
                    if (!this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= CRAFT_SLOTS && slot < INVENTORY_SLOTS) {
                    if (!this.insertItem(itemStack2, INVENTORY_SLOTS, TOTAL_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 29 && slot < TOTAL_SLOTS && !this.insertItem(itemStack2, CRAFT_SLOTS, INVENTORY_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, CRAFT_SLOTS, TOTAL_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    protected boolean isHauntable(ItemStack itemStack) {
        return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SingleStackRecipeInput(itemStack), this.world).isPresent();
    }

    public int getCookProgress() {
        int i = this.propertyDelegate.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_PROPERTY_INDEX);
        int j = this.propertyDelegate.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_TOTAL_PROPERTY_INDEX);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public boolean isBurning() {
        return this.propertyDelegate.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_PROPERTY_INDEX) > 0;
    }

    public int energyLevel() {
        // the power meter is 32 pixels tall
        return (32 * this.propertyDelegate.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX)) / PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY;
    }

    public int energy() {
       return this.propertyDelegate.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX);
    }

    public RecipeBookCategory getCategory() {
        return this.category;
    }

    public boolean canInsertIntoSlot(int index) {
        return index != OUTPUT_SLOT;
    }
}
