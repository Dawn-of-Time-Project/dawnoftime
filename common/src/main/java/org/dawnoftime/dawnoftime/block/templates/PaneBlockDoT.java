package org.dawnoftime.dawnoftime.block.templates;

import org.dawnoftime.dawnoftime.block.IBlockTooltip;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaneBlockDoT extends IronBarsBlock implements IBlockTooltip {
    private final String[] tooltipKeys;

    public PaneBlockDoT(Properties properties, String... tooltipKeys) {
        super(properties);
        this.tooltipKeys = tooltipKeys != null ? tooltipKeys : new String[0];
    }

    public PaneBlockDoT(Properties properties) {
        this(properties, (String[]) null);
    }

    protected final VoxelShape[] shapeByIndex = makeShapesByIndex(1.0F, 1.0F, 16.0F, 0.0F, 16.0F);

    protected static VoxelShape[] makeShapesByIndex(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
        final float f = 8.0F - nodeWidth;
        final float f1 = 8.0F + nodeWidth;
        final float f2 = 8.0F - extensionWidth;
        final float f3 = 8.0F + extensionWidth;
        final VoxelShape post = Block.box(f, 0.0D, f, f1, nodeHeight, f1);
        final VoxelShape north = Block.box(f2, extensionBottom, 0.0D, f3, extensionHeight, f3);
        final VoxelShape south = Block.box(f2, extensionBottom, f2, f3, extensionHeight, 16.0D);
        final VoxelShape west = Block.box(0.0D, extensionBottom, f2, f3, extensionHeight, f3);
        final VoxelShape east = Block.box(f2, extensionBottom, f2, 16.0D, extensionHeight, f3);
        final VoxelShape northEast = Shapes.or(north, east);
        final VoxelShape southEast = Shapes.or(south, east);
        final VoxelShape southWest = Shapes.or(south, west);
        final VoxelShape[] shapes = new VoxelShape[]{Shapes.empty(), south, west, southWest, north, Shapes.or(south, north), Shapes.or(west, north), Shapes.or(southWest, north), east, southEast, Shapes.or(west, east), Shapes.or(southWest, east), northEast, Shapes.or(southEast, north), Shapes.or(west, northEast), Shapes.or(southWest, northEast)};
        for (int i = 0; i < 16; ++i) {
            shapes[i] = Shapes.or(post, shapes[i]);
        }
        return shapes;
    }

    protected int getAABBIndex(BlockState state) {
        int index = 0;
        if (state.getValue(NORTH)) index |= 1 << Direction.NORTH.get2DDataValue();
        if (state.getValue(EAST)) index |= 1 << Direction.EAST.get2DDataValue();
        if (state.getValue(SOUTH)) index |= 1 << Direction.SOUTH.get2DDataValue();
        if (state.getValue(WEST)) index |= 1 << Direction.WEST.get2DDataValue();
        return index;
    }


    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        for (String key : tooltipKeys) {
            tooltip.add(Component.translatable(key));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Fluid fluid = context.getLevel().getFluidState(context.getClickedPos()).getType();
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posWest = pos.west();
        BlockPos posEast = pos.east();
        return this.defaultBlockState()
                .setValue(NORTH, this.canAttachPane(world, posNorth, Direction.SOUTH, world.getBlockState(posNorth)))
                .setValue(SOUTH, this.canAttachPane(world, posSouth, Direction.NORTH, world.getBlockState(posSouth)))
                .setValue(WEST, this.canAttachPane(world, posWest,  Direction.EAST, world.getBlockState(posWest)))
                .setValue(EAST, this.canAttachPane(world, posEast,  Direction.WEST, world.getBlockState(posEast)))
                .setValue(WATERLOGGED, fluid == Fluids.WATER);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if(adjacentBlockState.getBlock() instanceof PaneBlockDoT) return false;
        return super.skipRendering(state, adjacentBlockState, side);
    }

    @Override
    protected BlockState updateShape(BlockState stateIn, LevelReader worldIn, ScheduledTickAccess ticksIn_, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource randomIn_) {
        // Override was required because IronBarsBlock#attachsTo() is final (???) and I need to allow connection to CenteredDoors.
        if(stateIn.getValue(WATERLOGGED))
            ticksIn_.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        return facing.getAxis().isHorizontal() ? stateIn.setValue(PROPERTY_BY_DIRECTION.get(facing), this.canAttachPane(worldIn, facingPos, facing.getOpposite(), facingState)) : stateIn;
    }

    public boolean canAttachPane(LevelReader level, BlockPos pos, Direction dir, BlockState adjacentState) {
        Block block = adjacentState.getBlock();
        if(block instanceof IronBarsBlock || adjacentState.is(BlockTags.WALLS)){
            return true;
        }else if(block instanceof CenteredDoorBlock){
            return adjacentState.getValue(DoorBlock.FACING).getAxis() != dir.getAxis();
        }else{
            return !isExceptionForConnection(adjacentState) && adjacentState.isFaceSturdy(level, pos, dir);
        }
    }
}
