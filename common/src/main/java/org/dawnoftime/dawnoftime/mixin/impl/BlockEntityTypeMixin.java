package org.dawnoftime.dawnoftime.mixin.impl;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.dawnoftime.registry.DoTBBlocksRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets the vanilla bed block entity accept our futons.
 * An injector is used instead of an overwrite so that other mods doing the same can coexist.
 */
@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("RETURN"), cancellable = true)
    private void dawnoftime$allowFutons(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && (Object) this == BlockEntityType.BED && DoTBBlocksRegistry.INSTANCE != null && state.is(DoTBBlocksRegistry.INSTANCE.LIGHT_GRAY_FUTON.get())) {
            cir.setReturnValue(true);
        }
    }
}
