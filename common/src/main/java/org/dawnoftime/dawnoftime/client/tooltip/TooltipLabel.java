package org.dawnoftime.dawnoftime.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

/**
 * Families of blocks sharing a behavior that is worth a coloured label in the tooltip.
 * The colors are applied here, never in the lang file.
 */
public enum TooltipLabel {
    COLUMN("column_label", "column"),
    CONNECTED_TEXTURE("connected_texture_label", "connected_texture"),
    CROSS_CONNECTED("cross_connected_label", "cross_connected"),
    DYNAMIC_MODEL("dynamic_model_label", "dynamic_model");

    public static final String KEY_PREFIX = "tooltip." + MOD_ID + ".";

    private final String labelKey;
    private final String descriptionKey;

    TooltipLabel(String labelKey, String descriptionKey) {
        this.labelKey = KEY_PREFIX + labelKey;
        this.descriptionKey = KEY_PREFIX + descriptionKey;
    }

    public Component getLabel() {
        return Component.translatable(this.labelKey).withStyle(ChatFormatting.GOLD);
    }

    public Component getDescription(Object... args) {
        return TooltipText.of(this.descriptionKey, args);
    }
}
