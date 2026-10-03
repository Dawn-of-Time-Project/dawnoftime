package org.dawnoftime.dawnoftime.item.templates;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.dawnoftime.block.IBlockTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DoTBBlockItem extends BlockItem {
    public DoTBBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        if (this.getBlock() instanceof IBlockTooltip provider) {
            final List<Component> lines = new ArrayList<>();
            provider.appendHoverText(stack, null, lines, flag);
            lines.forEach(builder);
        }
    }
}
