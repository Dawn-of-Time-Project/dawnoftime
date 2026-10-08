package org.dawnoftime.dawnoftime;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import org.dawnoftime.dawnoftime.client.tooltip.BlockTooltips;

public class DoTBFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        DoTBCommon.init();
        RegistryImpls.init();
    }

    @Override
    public void onInitializeClient() {
        RegistryImpls.initClient();
        RenderLayers.init();
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> BlockTooltips.append(stack, lines));
    }
}
