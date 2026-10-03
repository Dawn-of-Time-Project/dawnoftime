package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.dawnoftime.dawnoftime.client.gui.creative.CreativeInventoryCategories;
import org.dawnoftime.dawnoftime.mixin.api.CreativeScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.dawnoftime.dawnoftime.DoTBCommon.CREATIVE_ICONS;
import static org.dawnoftime.dawnoftime.DoTBCommon.MOD_ID;

public class CategoryButton extends Button {
    private final CreativeScreen parent;
    private boolean selected;
    private static final Identifier[] BUTTON_ICONS = fillButtonIcons();
    private static final Tooltip[] BUTTON_TOOLTIPS = fillButtonTooltips();
    private final int index;
    private @Nullable Tooltip currentTooltip;

    public CategoryButton(int x, int y, int index, OnPress pressable, CreativeScreen parent) {
        super(x, y, 32, 28, Component.empty(), pressable, DEFAULT_NARRATION);
        this.selected = false;
        this.index = index;
        this.parent = parent;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public int getCategoryID() {
        return parent.dOTBuilder$getPage() * 4 + this.index;
    }

    public @Nullable Tooltip getTooltipForCategory() {
        int id = this.getCategoryID();
        if (id < BUTTON_TOOLTIPS.length && id >= 0) {
            return BUTTON_TOOLTIPS[id];
        } else {
            return null;
        }
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        final Tooltip tooltip = this.active ? this.getTooltipForCategory() : null;
        if (tooltip != this.currentTooltip) {
            this.currentTooltip = tooltip;
            this.setTooltip(tooltip);
        }

        if (this.active) {
            final int color = ARGB.white(this.alpha);
            graphics.blit(RenderPipelines.GUI_TEXTURED, CREATIVE_ICONS, this.getX() - 1, this.getY(), 0.0F, this.selected ? 0.0F : 28.0F, 31, 28, 256, 256, color);
            final int id = this.getCategoryID();
            if (id >= 0 && id < BUTTON_ICONS.length) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, BUTTON_ICONS[id], this.getX() + (this.selected ? 6 : 9), this.getY() + 6, 0.0F, 0.0F, 16, 16, 16, 16, color);
            }
        }
    }

    private static Identifier[] fillButtonIcons() {
        int number = CreativeInventoryCategories.values().length;
        Identifier[] table = new Identifier[number];
        for(int i = 0; i < number; i++) {
            table[i] = Identifier.fromNamespaceAndPath(MOD_ID, "textures/item/logo_" + CreativeInventoryCategories.values()[i].getName() + ".png");
        }
        return table;
    }

    private static Tooltip[] fillButtonTooltips() {
        int number = CreativeInventoryCategories.values().length;
        Tooltip[] tooltips = new Tooltip[number];
        for(int i = 0; i < number; i++) {
            tooltips[i] = Tooltip.create(Component.translatable("gui.dawnoftimebuilder." + CreativeInventoryCategories.values()[i].getName()));
        }
        return tooltips;
    }
}
