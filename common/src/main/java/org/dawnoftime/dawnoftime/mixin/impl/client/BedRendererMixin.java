package org.dawnoftime.dawnoftime.mixin.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.dawnoftime.dawnoftime.registry.DoTBBlocksRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The futon shares the vanilla bed block entity, so the vanilla bed renderer would draw a bed on top of it.
 * The futon has its own block model, so the vanilla rendering is skipped for it only.
 * An injector is used (never an overwrite) and a missing target is tolerated.
 */
@Mixin(BedRenderer.class)
public class BedRendererMixin {

    @Inject(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BedRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void dawnoftime$skipFutons(BedRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        if (DoTBBlocksRegistry.INSTANCE != null && Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.getBlockState(state.blockPos).is(DoTBBlocksRegistry.INSTANCE.LIGHT_GRAY_FUTON.get())) {
            ci.cancel();
        }
    }
}
