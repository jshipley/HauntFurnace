package com.jship;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PoweredHauntFurnaceBlock extends BlockWithEntity {
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;

    public PoweredHauntFurnaceBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(LIT, false));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PoweredHauntFurnaceBlockEntity(pos, state);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, HauntFurnace.POWERED_HAUNT_FURNACE_BLOCK_ENTITY);
    }

    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PoweredHauntFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(HauntFurnace.INTERACT_WITH_HAUNT_FURNACE);
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean) state.get(LIT)) {
            double d = (double) pos.getX() + 0.5;
            double e = (double) pos.getY();
            double f = (double) pos.getZ() + 0.5;

            Direction direction = (Direction) state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? (double) direction.getOffsetX() * g : h;
            // change j to change y level of particles? furnace = 6, blast furnace = 9
            double j = random.nextDouble() * 9.0 / 16.0;
            double k = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * g : h;
            world.addParticle(ParticleTypes.SOUL, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            this.openScreen(world, pos, player);
            return ActionResult.CONSUME;
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PoweredHauntFurnaceBlockEntity) {
                ((PoweredHauntFurnaceBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }

    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PoweredHauntFurnaceBlockEntity) {
                if (world instanceof ServerWorld) {
                    ItemScatterer.spawn(world, pos, (PoweredHauntFurnaceBlockEntity) blockEntity);
                    ((PoweredHauntFurnaceBlockEntity) blockEntity).getRecipesUsedAndDropExperience((ServerWorld) world, Vec3d.ofCenter(pos));
                }

                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    // From https://maven.fabricmc.net/docs/yarn-1.20.1+build.1/net/minecraft/block/AbstractBlock.html#deprecated-methods
    // Overriding the methods is an expected usage and is not deprecated in any way.
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, LIT});
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends PoweredHauntFurnaceBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, PoweredHauntFurnaceBlockEntity::tick);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        LIT = Properties.LIT;
    }
}
