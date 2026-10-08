package org.dawnoftime.dawnoftime.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.dawnoftime.dawnoftime.client.gui.GuiTexts;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.dawnoftime.menu.StoneOvenMenu;
import org.dawnoftime.dawnoftime.registry.DoTBBlockEntitiesRegistry;

public class StoneOvenBlockEntity extends AbstractFurnaceBlockEntity {
    public StoneOvenBlockEntity(BlockPos pos, BlockState state) {
        super(DoTBBlockEntitiesRegistry.INSTANCE.STONE_OVEN.get(), pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
        return GuiTexts.stoneOven();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new StoneOvenMenu(containerId, playerInventory, this, this.dataAccess);
    }
}
