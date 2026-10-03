package org.dawnoftime.dawnoftime.registry;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DoTBColorsRegistry {
    private static final Map<BlockTintSource, List<Supplier<Block>>> BLOCKS_COLOR_REGISTRY = new LinkedHashMap<>();

    public static final BlockTintSource WATER_BLOCK_COLOR = DoTBColorsRegistry.register(BlockTintSources.water(),
            DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_FAUCET, DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_POOL, DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_SMALL_POOL, DoTBBlocksRegistry.INSTANCE.WATER_FLOWING_TRICKLE,
            DoTBBlocksRegistry.INSTANCE.WATER_SOURCE_TRICKLE, DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_WATER_JET,
            DoTBBlocksRegistry.INSTANCE.SANDSTONE_FAUCET, DoTBBlocksRegistry.INSTANCE.SANDSTONE_POOL, DoTBBlocksRegistry.INSTANCE.SANDSTONE_SMALL_POOL, DoTBBlocksRegistry.INSTANCE.SANDSTONE_WATER_JET);

    public static final BlockTintSource LEAVES_BLOCK_COLOR = DoTBColorsRegistry.register(BlockTintSources.foliage(),
            DoTBBlocksRegistry.INSTANCE.ACACIA_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.ACACIA_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.BIRCH_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.BIRCH_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.DARK_OAK_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.DARK_OAK_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.JUNGLE_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.JUNGLE_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.MANGROVE_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.MANGROVE_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.OAK_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.OAK_LEAVES_PLATE,
            DoTBBlocksRegistry.INSTANCE.SPRUCE_LEAVES_EDGE,
            DoTBBlocksRegistry.INSTANCE.SPRUCE_LEAVES_PLATE
    );

    public static Map<BlockTintSource, List<Supplier<Block>>> getBlocksColorRegistry() {
        return BLOCKS_COLOR_REGISTRY;
    }

    @SafeVarargs
    private static BlockTintSource register(final BlockTintSource tintSource, final Supplier<Block>... blocksIn) {
        final List<Supplier<Block>> blocks = DoTBColorsRegistry.BLOCKS_COLOR_REGISTRY.computeIfAbsent(tintSource, key -> new ArrayList<>());
        Collections.addAll(blocks, blocksIn);
        return tintSource;
    }

    public static void initialize() {
    }
}
