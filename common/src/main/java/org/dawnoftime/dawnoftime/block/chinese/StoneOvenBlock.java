package org.dawnoftime.dawnoftime.block.chinese;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.dawnoftime.dawnoftime.blockentity.StoneOvenBlockEntity;
import org.dawnoftime.dawnoftime.registry.DoTBBlockEntitiesRegistry;

import javax.annotation.Nullable;

public class StoneOvenBlock extends AbstractFurnaceBlock {
    public StoneOvenBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoneOvenBlockEntity(pos, state);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof AbstractFurnaceBlockEntity) {
            player.openMenu((AbstractFurnaceBlockEntity) be);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, DoTBBlockEntitiesRegistry.INSTANCE.STONE_OVEN.get());
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        super.animateTick(stateIn, worldIn, pos, rand);
        if (stateIn.getValue(BlockStateProperties.LIT)) {
            worldIn.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    pos.getX() + 0.5D,
                    pos.getY() + 1.0D,
                    pos.getZ() + 0.5D,
                    0.0D, 0.07D, 0.0D);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        if (clicked.getAxis().isHorizontal()) {
            return this.defaultBlockState().setValue(FACING, clicked.getOpposite());
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return type != PathComputationType.LAND && super.isPathfindable(state, level, pos, type);
    }
}
