package org.dawnoftime.dawnoftime.block.japanese;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.dawnoftime.dawnoftime.block.templates.BlockDoT;
import org.dawnoftime.dawnoftime.registry.DoTBBlocksRegistry;
import org.dawnoftime.dawnoftime.util.VoxelShapes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TatamiFloorBlock extends BlockDoT {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public TatamiFloorBlock(Properties properties) {
        super(properties.pushReaction(PushReaction.DESTROY), VoxelShapes.TATAMI_FLOOR_SHAPES);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.TOP));
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(DoTBBlocksRegistry.INSTANCE.TATAMI_MAT.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF, FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext contextIn) {
        final Level level = contextIn.getLevel();
        final BlockPos pos = contextIn.getClickedPos();
        final BlockState currentState = level.getBlockState(pos);
        return currentState.is(this) ? currentState : this.defaultBlockState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack useStack_, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(!worldIn.isClientSide()) {
            if(player.isCrouching()) {
                boolean isTop = state.getValue(HALF) == Half.TOP;
                BlockPos otherPos = (isTop) ? pos.relative(state.getValue(FACING)) : pos.relative(state.getValue(FACING).getOpposite());
                if(isTop)//Check if the blocks above each part are AIR
                    if(!worldIn.isEmptyBlock(pos.above()))
                        return InteractionResult.PASS;
                    else if(!worldIn.isEmptyBlock(otherPos.above()))
                        return InteractionResult.PASS;
                worldIn.setBlock(pos, Blocks.SPRUCE_PLANKS.defaultBlockState(), 2);
                worldIn.setBlock(otherPos, Blocks.SPRUCE_PLANKS.defaultBlockState(), 2);
                worldIn.setBlock((isTop) ? pos.above() : otherPos.above(), DoTBBlocksRegistry.INSTANCE.TATAMI_MAT.get().defaultBlockState().setValue(TatamiMatBlock.HALF, Half.TOP).setValue(TatamiMatBlock.FACING, state.getValue(FACING)).setValue(TatamiMatBlock.ROLLED, true), 2);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, boolean isMoving) {
        Direction facing = state.getValue(FACING);
        Half half = state.getValue(HALF);
        BlockPos otherPos = (half == Half.TOP) ? pos.relative(facing) : pos.relative(facing.getOpposite());
        if(half.equals(Half.TOP)) {
            Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(DoTBBlocksRegistry.INSTANCE.TATAMI_MAT.get().asItem(), 1));
        }

        world.setBlock(otherPos, Blocks.SPRUCE_PLANKS.defaultBlockState(), 10);
        world.setBlock(pos, Blocks.SPRUCE_PLANKS.defaultBlockState(), 10);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT:
                return state.setValue(FACING, state.getValue(FACING).getOpposite()).setValue(HALF, (state.getValue(HALF) == Half.TOP) ? Half.BOTTOM : Half.TOP);
            case FRONT_BACK:
                return state.setValue(FACING, state.getValue(FACING).getOpposite());
            default:
                return super.mirror(state, mirrorIn);
        }
    }
}