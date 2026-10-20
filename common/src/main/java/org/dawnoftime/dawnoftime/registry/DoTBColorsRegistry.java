package org.dawnoftime.dawnoftime.registry;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Block tints only. Since 1.21.4 item tints are declared in the item definition (assets/modid/items/*.json) with a tint source.
 */
public class DoTBColorsRegistry {
    private static final Map<BlockTintSource, List<Supplier<Block>>> BLOCKS_COLOR_REGISTRY = new HashMap<>();

    public static final BlockTintSource WATER_BLOCK_COLOR = DoTBColorsRegistry.register(BlockTintSources.water(),
            DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_FAUCET,
            DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_POOL,
            DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_SMALL_POOL,
            DoTBBlocksRegistry.INSTANCE.WATER_FLOWING_TRICKLE,
            DoTBBlocksRegistry.INSTANCE.WATER_SOURCE_TRICKLE,
            DoTBBlocksRegistry.INSTANCE.STONE_BRICKS_WATER_JET,
            DoTBBlocksRegistry.INSTANCE.SANDSTONE_FAUCET,
            DoTBBlocksRegistry.INSTANCE.SANDSTONE_POOL,
            DoTBBlocksRegistry.INSTANCE.SANDSTONE_SMALL_POOL,
            DoTBBlocksRegistry.INSTANCE.SANDSTONE_WATER_JET
    );

    public static Map<BlockTintSource, List<Supplier<Block>>> getBlocksColorRegistry() {
        return BLOCKS_COLOR_REGISTRY;
    }

    @SafeVarargs
    private static BlockTintSource register(final BlockTintSource blockColorIn, final Supplier<Block>... blocksIn) {
        List<Supplier<Block>> blocks = DoTBColorsRegistry.getBlocks(blockColorIn);
        if (blocks == null) {
            blocks = new ArrayList<>();
            DoTBColorsRegistry.BLOCKS_COLOR_REGISTRY.put(blockColorIn, blocks);
        }
        Collections.addAll(blocks, blocksIn);
        return blockColorIn;
    }

    private static List<Supplier<Block>> getBlocks(final BlockTintSource blockColorIn) {
        for (final Entry<BlockTintSource, List<Supplier<Block>>> entry : DoTBColorsRegistry.BLOCKS_COLOR_REGISTRY.entrySet()) {
            if (entry.getKey().getClass() == blockColorIn.getClass()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void initialize() {}
}