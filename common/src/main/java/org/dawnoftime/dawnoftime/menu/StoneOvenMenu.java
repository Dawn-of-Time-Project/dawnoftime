package org.dawnoftime.dawnoftime.menu;

import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.RecipeType;
import org.dawnoftime.dawnoftime.registry.DoTBMenusRegistry;

public class StoneOvenMenu extends AbstractFurnaceMenu {
    public StoneOvenMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public StoneOvenMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData) {
        super(DoTBMenusRegistry.INSTANCE.STONE_OVEN.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory, furnaceContainer, furnaceData);
    }
}
