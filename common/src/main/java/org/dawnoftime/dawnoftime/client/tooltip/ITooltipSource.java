package org.dawnoftime.dawnoftime.client.tooltip;

import java.util.List;

/**
 * Implemented by block classes whose behavior comes with a tooltip.
 * The tooltip is assembled in {@link BlockTooltips}: labels first, then class texts, then the unique texts of the block.
 */
public interface ITooltipSource {

    /**
     * @return The labels earned by the behavior of this class.
     */
    default List<TooltipLabel> getTooltipLabels() {
        return List.of();
    }

    /**
     * @return The lang keys (without the "tooltip.dawnoftimebuilder." prefix) of the texts shared by every block of this class.
     */
    default List<String> getTooltipTexts() {
        return List.of();
    }
}
