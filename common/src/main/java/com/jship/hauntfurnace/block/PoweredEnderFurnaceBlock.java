package com.jship.hauntfurnace.block;

import org.jetbrains.annotations.Nullable;

import com.jship.hauntfurnace.HauntFurnace;
import com.jship.hauntfurnace.HauntFurnace.ModBlockEntities;
import com.jship.hauntfurnace.block.entity.PoweredEnderFurnaceBlockEntity;
import com.jship.hauntfurnace.config.HauntFurnaceConfig;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.BlockState;

public class PoweredEnderFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<PoweredEnderFurnaceBlock> CODEC = PoweredEnderFurnaceBlock.simpleCodec(PoweredEnderFurnaceBlock::new);

    public PoweredEnderFurnaceBlock(Properties properties) {
        super(properties);
    }

    public MapCodec<PoweredEnderFurnaceBlock> codec() {
        return CODEC;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PoweredEnderFurnaceBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
            BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null
                : AbstractFurnaceBlock.createTickerHelper(blockEntityType, ModBlockEntities.POWERED_ENDER_FURNACE.get(),
                        PoweredEnderFurnaceBlockEntity::serverTick);
    }

    @Override
    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof PoweredEnderFurnaceBlockEntity) {
            player.openMenu((MenuProvider) blockEntity);
            // player.awardStat(HauntFurnace.INTERACT_WITH_ENDER_FURNACE);
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
            if (randomSource.nextDouble() < 0.02 && !HauntFurnaceConfig.enderQuiet()) {
                level.playLocalSound(x, y, z, SoundEvents.ENDER_DRAGON_AMBIENT, SoundSource.BLOCKS, 0.05F, 0.05F, false);
            }

            Direction direction = (Direction) blockState.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = randomSource.nextDouble() * 0.6 - 0.3;
            double xd = axis == Axis.X ? (double) direction.getStepX() * g : h;
            double yd = randomSource.nextDouble() * 9.0 / 16.0;
            double zd = axis == Axis.Z ? (double) direction.getStepZ() * g : h;
            level.addParticle(ParticleTypes.SMOKE, x + xd, y + yd, z + zd, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.DRAGON_BREATH, x + xd, y + yd, z + zd, 0.0, 0.0, 0.0);
        }
    }
}
