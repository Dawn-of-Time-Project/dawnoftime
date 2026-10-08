package org.dawnoftime.dawnoftime.mixin.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.world.level.block.entity.BedBlockEntity;
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

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BedBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true, require = 0)
    private void dawnoftime$skipFutons(BedBlockEntity bed, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (DoTBBlocksRegistry.INSTANCE != null && bed.getBlockState().is(DoTBBlocksRegistry.INSTANCE.LIGHT_GRAY_FUTON.get())) {
            ci.cancel();
        }
    }
}
