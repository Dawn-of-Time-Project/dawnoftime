package org.dawnoftime.dawnoftime.block.templates;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class WallOrFloorBlockDoT extends BlockDoT {

    // UP = floor. NORTH/SOUTH/EAST/WEST = the wall face the carpet is visible from.
    public static final EnumProperty<Direction> PLACED_FACE = EnumProperty.create("placed_face", Direction.class,
        Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    // shapes[0]=floor, [1]=SOUTH wall, [2]=WEST wall, [3]=NORTH wall, [4]=EAST wall
    public WallOrFloorBlockDoT(Properties properties, VoxelShape[] shapes, String... tooltipKeys) {
        super(properties, shapes, tooltipKeys);
        this.registerDefaultState(this.defaultBlockState().setValue(PLACED_FACE, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PLACED_FACE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        // DOWN = clicked from below, redirect to floor
        if (clicked == Direction.DOWN) clicked = Direction.UP;
        return this.defaultBlockState().setValue(PLACED_FACE, clicked);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, @NotNull BlockPos pos) {
        Direction face = state.getValue(PLACED_FACE);
        if (face == Direction.UP) {
            // Floor: needs a solid block below
            return !worldIn.isEmptyBlock(pos.below());
        }
        // Wall: the supporting block is in the opposite direction of the visible face
        BlockPos supportPos = pos.relative(face.getOpposite());
        return worldIn.getBlockState(supportPos).isFaceSturdy(worldIn, supportPos, face);
    }

    @Override
    protected BlockState updateShape(BlockState stateIn, LevelReader worldIn, ScheduledTickAccess ticksIn_, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource randomIn_) {
        Direction face = stateIn.getValue(PLACED_FACE);
        // Determine the direction where the support block sits
        Direction supportDir = (face == Direction.UP) ? Direction.DOWN : face.getOpposite();
        if (facing == supportDir && !stateIn.canSurvive(worldIn, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return stateIn;
    }

    @Override
    public int getShapeIndex(@NotNull BlockState state, @NotNull BlockGetter level,
            @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(PLACED_FACE)) {
            case NORTH -> 1;
            case EAST  -> 2;
            case SOUTH -> 3;
            case WEST  -> 4;
            default    -> 0; // UP = floor
        };
    }
}
