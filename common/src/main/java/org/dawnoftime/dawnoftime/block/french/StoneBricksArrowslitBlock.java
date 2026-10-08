package org.dawnoftime.dawnoftime.block.french;

import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.dawnoftime.block.templates.WaterloggedHorizontalBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import org.dawnoftime.dawnoftime.client.tooltip.ITooltipSource;

public class StoneBricksArrowslitBlock extends WaterloggedHorizontalBlock implements ITooltipSource {
    public StoneBricksArrowslitBlock(Properties properties, VoxelShape[] shapes) {
        super(properties, shapes);
    }

    @Override
    public List<String> getTooltipTexts() {
        return List.of("stone_bricks_defense");
    }
}
