package com.jship.hauntfurnace.menu;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class PoweredHauntFurnaceMenu extends RecipeBookMenu<SingleRecipeInput, AbstractCookingRecipe> {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int CRAFT_SLOTS = 2;
    public static final int INVENTORY_SLOTS = 29;
    public static final int TOTAL_SLOTS = 38;
    private final Container container;
    private final ContainerData dataAccess;
    protected final Level level;
    private final RecipeBookType recipeBookType;
    public PoweredHauntFurnaceMenu(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(CRAFT_SLOTS), new SimpleContainerData(PoweredHauntFurnaceBlockEntity.PROPERTY_COUNT));
    }

    public PoweredHauntFurnaceMenu(int syncId, Inventory inventory, Container container, ContainerData dataAccess) {
        super(HauntFurnace.POWERED_HAUNT_FURNACE_MENU, syncId);
        this.recipeBookType = RecipeBookType.FURNACE;
        checkContainerSize(container, CRAFT_SLOTS);
        checkContainerDataCount(dataAccess, PoweredHauntFurnaceBlockEntity.PROPERTY_COUNT);
        this.container = container;
        this.dataAccess = dataAccess;
        this.level = inventory.player.level();
        this.addSlot(new Slot(container, INPUT_SLOT, 56, 35));
        this.addSlot(new FurnaceResultSlot(inventory.player, container, OUTPUT_SLOT, 116, 35));

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }

        this.addDataSlots(dataAccess);
    }

    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(stackedContents);
        }

    }

    public void clearCraftingContent() {
        this.getSlot(INPUT_SLOT).set(ItemStack.EMPTY);
        this.getSlot(OUTPUT_SLOT).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(RecipeHolder<AbstractCookingRecipe> recipe) {
        return recipe.value().matches(new SingleRecipeInput(this.container.getItem(0)), this.level);
    }

    public int getResultSlotIndex() {
        return OUTPUT_SLOT;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return CRAFT_SLOTS;
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot == OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemStack2, CRAFT_SLOTS, TOTAL_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickCraft(itemStack2, itemStack);
            } else if (slot != INPUT_SLOT) {
                if (this.isHauntable(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= CRAFT_SLOTS && slot < INVENTORY_SLOTS) {
                    if (!this.moveItemStackTo(itemStack2, INVENTORY_SLOTS, TOTAL_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 29 && slot < TOTAL_SLOTS && !this.moveItemStackTo(itemStack2, CRAFT_SLOTS, INVENTORY_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, CRAFT_SLOTS, TOTAL_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setByPlayer(ItemStack.EMPTY);
            } else {
                slot2.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTake(player, itemStack2);
        }

        return itemStack;
    }

    protected boolean isHauntable(ItemStack itemStack) {
        return this.level.getRecipeManager().getRecipeFor(HauntFurnace.HAUNTING_RECIPE, new SingleRecipeInput(itemStack), this.level).isPresent();
    }

    public int getCookProgress() {
        int i = this.dataAccess.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_PROPERTY_INDEX);
        int j = this.dataAccess.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_TOTAL_PROPERTY_INDEX);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public boolean isBurning() {
        return this.dataAccess.get(PoweredHauntFurnaceBlockEntity.COOK_TIME_PROPERTY_INDEX) > 0;
    }

    public int energyLevel() {
        // the power meter is 32 pixels tall
        return (32 * this.dataAccess.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX)) / PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY;
    }

    public int energy() {
       return this.dataAccess.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX);
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int index) {
        return index != OUTPUT_SLOT;
    }
}
