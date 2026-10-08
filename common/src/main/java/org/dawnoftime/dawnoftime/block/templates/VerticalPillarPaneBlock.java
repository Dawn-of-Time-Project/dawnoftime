package org.dawnoftime.dawnoftime.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.dawnoftime.dawnoftime.util.BlockStatePropertiesAA;
import org.jetbrains.annotations.NotNull;

public class VerticalPillarPaneBlock extends PillarPaneBlock {
    public static final EnumProperty<BlockStatePropertiesAA.VerticalConnection> VERTICAL_CONNECTION = BlockStatePropertiesAA.VERTICAL_CONNECTION;

    public VerticalPillarPaneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VERTICAL_CONNECTION, BlockStatePropertiesAA.VerticalConnection.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VERTICAL_CONNECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            state = state.setValue(VERTICAL_CONNECTION, this.getVerticalConnectionState(context.getLevel(), context.getClickedPos()));
        }
        return state;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        stateIn = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return facing.getAxis().isVertical() ? stateIn.setValue(VERTICAL_CONNECTION, this.getVerticalConnectionState(worldIn, currentPos)) : stateIn;
    }

    private BlockStatePropertiesAA.VerticalConnection getVerticalConnectionState(LevelAccessor world, BlockPos pos) {
        boolean above = world.getBlockState(pos.above()).getBlock() == this;
        boolean below = world.getBlockState(pos.below()).getBlock() == this;
        if (above && below) return BlockStatePropertiesAA.VerticalConnection.BOTH;
        if (above) return BlockStatePropertiesAA.VerticalConnection.ABOVE;
        if (below) return BlockStatePropertiesAA.VerticalConnection.UNDER;
        return BlockStatePropertiesAA.VerticalConnection.NONE;
    }
}
