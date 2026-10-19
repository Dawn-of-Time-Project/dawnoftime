package org.dawnoftime.dawnoftime.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

/**
 * Single entry point that builds the tooltip of every Dawn of Time block.
 * <ul>
 *     <li>Labels: from {@link ITooltipSource#getTooltipLabels()} (behavior coded in a class) and from {@link #CONNECTIONS} (behavior coming from the Fusion models).</li>
 *     <li>Class texts: from {@link ITooltipSource#getTooltipTexts()}.</li>
 *     <li>Unique texts: lang keys "tooltip.dawnoftimebuilder.block.[block id]", followed by ".1", ".2"... for several lines.</li>
 * </ul>
 */
public final class BlockTooltips {
    private static final String UNIQUE_PREFIX = TooltipLabel.KEY_PREFIX + "block.";

    private record Connection(TooltipLabel label, Component partners) {}

    /**
     * Blocks using a Fusion "connecting" model, by block id. Source of truth: fusion-overrides/assets/dawnoftimebuilder/models/block.
     */
    private static final Map<String, Connection> CONNECTIONS = new HashMap<>();

    static {
        // Connects with itself only
        connected("limestone_bricks");
        connected("waxed_oak_framed_rammed_dirt");
        connected("waxed_oak_framed_rammed_dirt_pillar");
        connected("waxed_oak_timber_frame_corner");
        connected("waxed_oak_timber_frame_crossed");
        connected("waxed_oak_timber_frame_pillar");
        connected("waxed_oak_timber_frame_squared");
        connected("yellow_oak_timber_frame_corner");
        connected("yellow_oak_timber_frame_crossed");
        connected("yellow_oak_timber_frame_pillar");
        connected("yellow_oak_timber_frame_squared");
        connected("weathered_oak_timber_frame_corner");
        connected("weathered_oak_timber_frame_crossed");
        connected("weathered_oak_timber_frame_pillar");
        connected("weathered_oak_timber_frame_squared");
        connected("red_oak_timber_frame_corner");
        connected("red_oak_timber_frame_crossed");
        connected("red_oak_timber_frame_pillar");
        connected("red_oak_timber_frame_squared");
        connected("spruce_timber_frame");
        connected("charred_spruce_timber_frame");
        connected("red_painted_timber_frame");
        connected("roman_fresco_black");
        connected("roman_fresco_red");
        connected("paper_wall_flat");
        connected("waxed_oak_table");

        // Connects with itself and other blocks
        crossWithBlock("moraq_mosaic_delicate", "moraq_mosaic_border");
        crossWithBlock("moraq_mosaic_border", "moraq_mosaic_delicate");
        crossWithBlock("moraq_mosaic_geometric", "moraq_mosaic_pattern");
        crossWithBlock("moraq_mosaic_pattern", "moraq_mosaic_geometric");
        crossWithBlock("mosaic_floor", "mosaic_floor_delicate");
        crossWithBlock("mosaic_floor_delicate", "mosaic_floor");
        crossWithBlock("persian_carpet_red", "persian_carpet_delicate_red");
        crossWithBlock("persian_carpet_delicate_red", "persian_carpet_red");
        crossWithGroup("waxed_oak_timber_frame", "waxed_oak_timber_frame_variants");
        crossWithGroup("yellow_oak_timber_frame", "yellow_oak_timber_frame_variants");
        crossWithGroup("weathered_oak_timber_frame", "weathered_oak_timber_frame_variants");
        crossWithGroup("red_oak_timber_frame", "red_oak_timber_frame_variants");
        crossWithGroup("stone_bricks_masonry", "stone_bricks_masonry_set");
        crossWithGroup("stone_bricks_masonry_plate", "stone_bricks_masonry_set");
        crossWithGroup("stone_bricks_masonry_slab", "stone_bricks_masonry_set");
        crossWithGroup("stone_bricks_masonry_wall", "stone_bricks_masonry_set");
    }

    private BlockTooltips() {}

    private static void connected(String blockId) {
        CONNECTIONS.put(blockId, new Connection(TooltipLabel.CONNECTED_TEXTURE, null));
    }

    private static void crossWithBlock(String blockId, String partnerId) {
        Component partner = Component.translatable("block." + MOD_ID + "." + partnerId).withStyle(ChatFormatting.AQUA);
        CONNECTIONS.put(blockId, new Connection(TooltipLabel.CROSS_CONNECTED, partner));
    }

    private static void crossWithGroup(String blockId, String groupId) {
        Component group = Component.translatable(TooltipLabel.KEY_PREFIX + "group." + groupId);
        CONNECTIONS.put(blockId, new Connection(TooltipLabel.CROSS_CONNECTED, group));
    }

    /**
     * Adds the tooltip of a Dawn of Time block right under the item name. Does nothing for any other item.
     */
    public static void append(ItemStack stack, List<Component> lines) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return;
        Block block = blockItem.getBlock();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (!MOD_ID.equals(id.getNamespace())) return;

        List<Component> tooltip = new ArrayList<>();
        ITooltipSource source = block instanceof ITooltipSource tooltipSource ? tooltipSource : null;

        if (source != null) {
            for (TooltipLabel label : source.getTooltipLabels()) {
                tooltip.add(label.getLabel());
                tooltip.add(label.getDescription());
            }
        }
        Connection connection = CONNECTIONS.get(id.getPath());
        if (connection != null) {
            tooltip.add(connection.label().getLabel());
            tooltip.add(connection.partners() == null ? connection.label().getDescription() : connection.label().getDescription(connection.partners()));
        }
        if (source != null) {
            for (String key : source.getTooltipTexts()) {
                tooltip.add(TooltipText.of(TooltipLabel.KEY_PREFIX + key));
            }
        }
        addUniqueTexts(tooltip, UNIQUE_PREFIX + id.getPath());

        lines.addAll(Math.min(1, lines.size()), tooltip);
    }

    private static void addUniqueTexts(List<Component> tooltip, String baseKey) {
        if (I18n.exists(baseKey)) {
            tooltip.add(TooltipText.of(baseKey));
        }
        for (int i = 1; I18n.exists(baseKey + "." + i); i++) {
            tooltip.add(TooltipText.of(baseKey + "." + i));
        }
    }
}
