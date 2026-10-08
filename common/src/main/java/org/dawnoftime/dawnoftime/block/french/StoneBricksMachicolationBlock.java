package org.dawnoftime.dawnoftime.block.french;

import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.dawnoftime.block.templates.ConnectedHorizontalBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import org.dawnoftime.dawnoftime.client.tooltip.ITooltipSource;

public class StoneBricksMachicolationBlock extends ConnectedHorizontalBlock implements ITooltipSource {
    public StoneBricksMachicolationBlock(Properties properties, VoxelShape[] shapes) {
        super(properties, shapes);
    }

    @Override
    public List<String> getTooltipTexts() {
        return List.of("stone_bricks_defense");
    }
}
