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
    // Not using this slot, but keeping the furnace slot numbering
    public static final int IGNORED_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int SLOT_COUNT = 3;
    public static final int DATA_COUNT = 5;
    public static final int INV_SLOT_START = 3;
    public static final int INV_SLOT_END = 30;
    public static final int USE_ROW_SLOT_START = 30;
    public static final int USE_ROW_SLOT_END = 39;
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
            new SimpleContainerData(PoweredHauntFurnaceBlockEntity.NUM_DATA_VALUES)
        );
    }

    public PoweredHauntFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(HauntFurnace.Menus.POWERED_HAUNT_FURNACE.get(), containerId);
        this.recipeBookType = RecipeBookType.FURNACE;
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, PoweredHauntFurnaceBlockEntity.NUM_DATA_VALUES);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level();
        this.acceptedInputs = this.level.recipeAccess().propertySet(HauntFurnace.Recipes.HAUNT_FURNACE_INPUT);
        this.addSlot(new Slot(container, INGREDIENT_SLOT, 56, 35));
        this.addSlot(new Slot(container, IGNORED_SLOT, 0, 0) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
        });
        this.addSlot(new FurnaceResultSlot(inventory.player, container, 2, 116, 35));
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
        return recipe.value().matches(new SingleRecipeInput(this.container.getItem(INGREDIENT_SLOT)), this.level);
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

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot) this.slots.get(index);
        if (slot2 != null && slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (index == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemStack2, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickCraft(itemStack2, itemStack);
            } else if (index != INGREDIENT_SLOT) {
                if (this.canHaunt(itemStack2)) {
                    if (!this.moveItemStackTo(itemStack2, 0, RESULT_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                    if (!this.moveItemStackTo(itemStack2, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (
                    index >= USE_ROW_SLOT_START &&
                    index < USE_ROW_SLOT_END &&
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
        int cookingTimer = this.data.get(PoweredHauntFurnaceBlockEntity.DATA_COOKING_PROGRESS);
        int cookingTotalTime = this.data.get(PoweredHauntFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
        return cookingTotalTime != 0 && cookingTimer != 0 ? (cookingTimer * 24) / cookingTotalTime : 0;
    }

    public boolean isLit() {
        return this.data.get(PoweredHauntFurnaceBlockEntity.DATA_LIT_TIME) > 0;
    }

    public int energyLevel() {
        // the power meter is 32 pixels tall
        return (
            (32 * this.data.get(PoweredHauntFurnaceBlockEntity.DATA_ENERGY_STORAGE)) /
            PoweredHauntFurnaceBlockEntity.ENERGY_CAPACITY
        );
    }

    public int energy() {
        return this.data.get(PoweredHauntFurnaceBlockEntity.DATA_ENERGY_STORAGE);
    }

    @Override
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
        List<Slot> list = List.of(this.getSlot(INGREDIENT_SLOT), this.getSlot(IGNORED_SLOT), this.getSlot(RESULT_SLOT));
        return ServerPlaceRecipe.placeRecipe(new PHCraftingMenuAccess(), this.getGridWidth(), this.getGridHeight(), List.of(this.getSlot(INGREDIENT_SLOT)), list, playerInventory, (RecipeHolder<HauntingRecipe>)recipe, useMaxItems, isCreative);
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
