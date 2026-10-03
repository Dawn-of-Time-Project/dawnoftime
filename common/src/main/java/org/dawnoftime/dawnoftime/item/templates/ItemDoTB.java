package org.dawnoftime.dawnoftime.item.templates;

import org.dawnoftime.dawnoftime.util.DoTBProperties;
import net.minecraft.world.item.Item;

public class ItemDoTB extends Item {

    public ItemDoTB() {
        this(DoTBProperties.item());
    }

    public ItemDoTB(Properties properties) {
        super(properties);
    }
}
