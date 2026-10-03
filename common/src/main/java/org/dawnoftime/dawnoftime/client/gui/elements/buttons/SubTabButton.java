package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class SubTabButton extends Button {
    private static final int HOVER_COLOR = 0xFFB3B3B3;

    private final Identifier textureOn;
    private final Identifier textureOff;
    private final Tooltip tooltip;
    private boolean selected;

    public SubTabButton(int x, int y, Identifier textureOn, Identifier textureOff, Component tooltip, OnPress pressable) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.textureOn = textureOn;
        this.textureOff = textureOff;
        this.tooltip = Tooltip.create(tooltip);
        this.setTooltip(this.tooltip);
        this.selected = false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.setTooltip(this.active ? this.tooltip : null);

        final int color = this.isHovered() && this.active ? HOVER_COLOR : 0xFFFFFFFF;
        final Identifier texture = this.selected ? this.textureOn : this.textureOff;
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX() - 1, this.getY(), 0.0F, 0.0F, 12, 12, 12, 12, color);
    }
}
