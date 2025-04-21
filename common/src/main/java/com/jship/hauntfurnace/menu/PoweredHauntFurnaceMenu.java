package com.jship.hauntfurnace.menu;

import java.util.List;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.block.entity.PoweredHauntFurnaceBlockEntity;
import com.jship.hauntfurnace.recipe.HauntingRecipe;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe.CraftingMenuAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class PoweredHauntFurnaceMenu extends RecipeBookMenu {

    public static final int INGREDIENT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int INV_SLOT_START = 2;
    public static final int INV_SLOT_END = 29;
    public static final int USE_ROW_SLOT_START = 29;
    public static final int USE_ROW_SLOT_END = 38;
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    private final RecipePropertySet acceptedInputs;
    private final RecipeBookType recipeBookType;

    public PoweredHauntFurnaceMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(SLOT_COUNT),
            new SimpleContainerData(PoweredHauntFurnaceBlockEntity.PROPERTY_COUNT)
        );
    }

    public PoweredHauntFurnaceMenu(int syncId, Inventory inventory, Container container, ContainerData data) {
        super(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), syncId);
        this.recipeBookType = RecipeBookType.FURNACE;
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, PoweredHauntFurnaceBlockEntity.PROPERTY_COUNT);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        this.acceptedInputs = this.level.recipeAccess().propertySet(HauntFurnace.Recipes.HAUNT_FURNACE_INPUT);
        this.addSlot(new Slot(container, INGREDIENT_SLOT, 56, 35));
        this.addSlot(new FurnaceResultSlot(inventory.player, container, RESULT_SLOT, 116, 35));
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlots(data);
    }

    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible) this.container).fillStackedContents(stackedItemContents);
        }
    }

    public void clearCraftingContent() {
        this.getSlot(INGREDIENT_SLOT).set(ItemStack.EMPTY);
        this.getSlot(RESULT_SLOT).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(RecipeHolder<HauntingRecipe> recipe) {
        return recipe.value().matches(new SingleRecipeInput(this.container.getItem(0)), this.level);
    }

    public Slot getResultSlot() {
        return (Slot)this.slots.get(RESULT_SLOT);
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return SLOT_COUNT;
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot) this.slots.get(slot);
        if (slot2 != null && slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemStack2, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickCraft(itemStack2, itemStack);
            } else if (slot != INGREDIENT_SLOT) {
                if (this.canHaunt(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= INV_SLOT_START && slot < INV_SLOT_END) {
                    if (!this.moveItemStackTo(itemStack2, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (
                    slot >= USE_ROW_SLOT_START &&
                    slot < USE_ROW_SLOT_END &&
                    !this.moveItemStackTo(itemStack2, INV_SLOT_START, INV_SLOT_END, false)
                ) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
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

    protected boolean canHaunt(ItemStack stack) {
        return this.acceptedInputs.test(stack);
    }

    public int getHauntProgress() {
        int i = this.data.get(PoweredHauntFurnaceBlockEntity.HAUNT_TIME_PROPERTY_INDEX);
        int j = this.data.get(PoweredHauntFurnaceBlockEntity.HAUNT_TIME_TOTAL_PROPERTY_INDEX);
        return j != 0 && i != 0 ? (i * 24) / j : 0;
    }

    public boolean isLit() {
        return this.data.get(PoweredHauntFurnaceBlockEntity.HAUNT_TIME_PROPERTY_INDEX) > 0;
    }

    public int energyLevel() {
        // the power meter is 32 pixels tall
        return (
            (32 * this.data.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX)) /
            PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY
        );
    }

    public int energy() {
        return this.data.get(PoweredHauntFurnaceBlockEntity.ENERGY_STORAGE_PROPERTY_INDEX);
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    @Override
    public PostPlaceAction handlePlacement(
        boolean useMaxItems,
        boolean isCreative,
        RecipeHolder<?> recipe,
        ServerLevel level,
        Inventory playerInventory
    ) {
        List<Slot> list = List.of(this.getSlot(0), this.getSlot(1));        
        return ServerPlaceRecipe.placeRecipe(new PHCraftingMenuAccess(), this.getGridWidth(), this.getGridHeight(), List.of(this.getSlot(0)), list, playerInventory, (RecipeHolder<HauntingRecipe>)recipe, useMaxItems, isCreative);
    }

    private class PHCraftingMenuAccess implements CraftingMenuAccess<HauntingRecipe> {

        @Override
        public void clearCraftingContent() {
            PoweredHauntFurnaceMenu.this.clearCraftingContent();
        }

        @Override
        public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
            PoweredHauntFurnaceMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
        }

        @Override
        public boolean recipeMatches(RecipeHolder<HauntingRecipe> recipe) {
            return PoweredHauntFurnaceMenu.this.recipeMatches(recipe);
        }
    }
}
