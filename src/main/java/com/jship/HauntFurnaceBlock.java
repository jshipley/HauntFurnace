package com.jship;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HauntFurnaceBlock extends AbstractFurnaceBlock {

    public static final MapCodec<HauntFurnaceBlock> CODEC;

    public HauntFurnaceBlock(Settings settings) {
        super(settings);
    }

    public MapCodec<HauntFurnaceBlock> getCodec() {
        return CODEC;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HauntFurnaceBlockEntity(pos, state);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return HauntFurnaceBlock.validateTicker(world, type, HauntFurnace.HAUNT_FURNACE_BLOCK_ENTITY);
    }

    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof HauntFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            player.incrementStat(HauntFurnace.INTERACT_WITH_HAUNT_FURNACE);
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = (Direction)state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * g : h;
            // change j to change y level of particles? furnace = 6, blast furnace = 9
            double j = random.nextDouble() * 9.0 / 16.0;
            double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * g : h;
            world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.SOUL, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }

    static {
        CODEC = HauntFurnaceBlock.createCodec(HauntFurnaceBlock::new);
    }
}
