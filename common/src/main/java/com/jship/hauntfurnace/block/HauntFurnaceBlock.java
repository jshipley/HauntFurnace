package com.jship.hauntfurnace.block;

import org.jetbrains.annotations.Nullable;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.block.entity.HauntFurnaceBlockEntity;
import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class HauntFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<HauntFurnaceBlock> CODEC = HauntFurnaceBlock.simpleCodec(HauntFurnaceBlock::new);

    public HauntFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public MapCodec<HauntFurnaceBlock> codec() {
        return CODEC;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HauntFurnaceBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(blockEntityType, ModBlockEntities.HAUNT_FURNACE.get(), (levelx, blockPos, blockStatex, hauntFurnaceBlockEntity) -> {
                HauntFurnaceBlockEntity.serverTick(serverLevel, blockPos, blockState, hauntFurnaceBlockEntity);
            });
        }
        return null;
    }

    @Override
    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof HauntFurnaceBlockEntity) {
            player.openMenu((MenuProvider) blockEntity);
            // player.awardStat(HauntFurnace.INTERACT_WITH_HAUNT_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if ((Boolean) blockState.getValue(LIT)) {
            double x = (double) blockPos.getX() + 0.5;
            double y = (double) blockPos.getY();
            double z = (double) blockPos.getZ() + 0.5;
            if (randomSource.nextDouble() < 0.1) {
                level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            if (randomSource.nextDouble() < 0.02 && !HauntFurnaceConfig.hauntQuiet()) {
                level.playLocalSound(x, y, z, SoundEvents.PHANTOM_AMBIENT, SoundSource.BLOCKS, 0.2F, 0.2F, false);
            }

            Direction direction = (Direction) blockState.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = randomSource.nextDouble() * 0.6 - 0.3;
            double xd = axis == Axis.X ? (double) direction.getStepX() * g : h;
            double yd = randomSource.nextDouble() * 6.0 / 16.0;
            double zd = axis == Axis.Z ? (double) direction.getStepZ() * g : h;
            level.addParticle(ParticleTypes.SMOKE, x + xd, y + yd, z + zd, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.SOUL, x + xd, y + yd, z + zd, 0.0, 0.0, 0.0);
        }
    }

}
