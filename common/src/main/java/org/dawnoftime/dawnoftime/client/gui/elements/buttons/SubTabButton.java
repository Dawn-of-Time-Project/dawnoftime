package org.dawnoftime.dawnoftime.client.gui.elements.buttons;

import net.minecraft.util.ARGB;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SubTabButton extends Button {
    private final Identifier textureOn;
    private final Identifier textureOff;
    private boolean selected;

    public SubTabButton(int x, int y, Identifier textureOn, Identifier textureOff, Component tooltip, OnPress pressable) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.textureOn = textureOn;
        this.textureOff = textureOff;
        this.setTooltip(Tooltip.create(tooltip));
        this.selected = false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int color = (this.isHovered() && this.active) ? ARGB.color(255, 179, 179, 179) : ARGB.white(this.alpha);
        Identifier texture = this.selected ? textureOn : textureOff;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX() - 1, this.getY(), 0.0F, 0.0F, 12, 12, 12, 12, color);
    }
}
