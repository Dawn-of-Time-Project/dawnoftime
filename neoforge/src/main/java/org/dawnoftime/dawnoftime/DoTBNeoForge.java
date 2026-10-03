package org.dawnoftime.dawnoftime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(DoTBCommon.MOD_ID)
public class DoTBNeoForge {
    public DoTBNeoForge(IEventBus modEventBus) {
        DoTBCommon.init();

        RegistryImpls.init(modEventBus);
    }
}
