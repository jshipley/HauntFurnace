package com.jship;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class HauntFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public HauntFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY, pos, state, HauntFurnace.HAUNTING_RECIPE);
    }

    protected Text getContainerName() {
        return Text.translatable("container.haunt_furnace");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HauntFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
