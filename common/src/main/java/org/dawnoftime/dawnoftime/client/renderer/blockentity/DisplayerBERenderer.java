package org.dawnoftime.dawnoftime.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.dawnoftime.dawnoftime.block.IBlockSpecialDisplay;
import org.dawnoftime.dawnoftime.block.templates.DisplayerBlock;
import org.dawnoftime.dawnoftime.blockentity.DisplayerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayerBERenderer implements BlockEntityRenderer<DisplayerBlockEntity, DisplayerBERenderer.DisplayerRenderState> {
	private static final int SLOTS = 9;
	private final ItemModelResolver itemModelResolver;

	public DisplayerBERenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public @NotNull DisplayerRenderState createRenderState() {
		return new DisplayerRenderState();
	}

	@Override
	public void extractRenderState(@NotNull DisplayerBlockEntity blockEntity, @NotNull DisplayerRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, breakProgress);
		state.blockState = blockEntity.getBlockState();
		int seed = (int) blockEntity.getBlockPos().asLong();
		for (int i = 0; i < SLOTS; i++) {
			ItemStack itemStack = blockEntity.getItem(i);
			ItemStackRenderState itemState = state.items[i];
			itemState.clear();
			state.blockItem[i] = false;
			state.displayScale[i] = 0.2F;
			state.displayOffsetY[i] = 0.45F;
			if (itemStack.isEmpty()) {
				continue;
			}
			Item item = itemStack.getItem();
			if (item instanceof BlockItem blockItem) {
				state.blockItem[i] = true;
				Block block = blockItem.getBlock();
				if (block instanceof IBlockSpecialDisplay specialDisplay) {
					state.displayScale[i] = specialDisplay.getDisplayScale();
					state.displayOffsetY[i] = 0.485F;
				}
				this.itemModelResolver.updateForTopItem(itemState, itemStack, ItemDisplayContext.NONE, blockEntity.getLevel(), null, i + seed);
			} else {
				this.itemModelResolver.updateForTopItem(itemState, itemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, i + seed);
			}
		}
	}

	@Override
	public void submit(@NotNull DisplayerRenderState state, @NotNull PoseStack stack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
		BlockState blockState = state.blockState;
		if (!(blockState.getBlock() instanceof DisplayerBlock block)) {
			return;
		}
		double xStart = block.getDisplayerX(blockState);
		double yStart = block.getDisplayerY(blockState);
		double zStart = block.getDisplayerZ(blockState);

		for (int i = 0; i < SLOTS; i++) {
			ItemStackRenderState itemState = state.items[i];
			if (itemState.isEmpty()) {
				continue;
			}
			float rotationAngle;
			if (i == 0 || i == 8) rotationAngle = 20.0F;
			else if (i == 2 || i == 6) rotationAngle = -20.0F;
			else rotationAngle = 0.0F;

			stack.pushPose();
			stack.translate(xStart, yStart, zStart);
			stack.translate((0.5D - xStart) * (i % 3), 0.015D, (0.5D - zStart) * Math.floor((double) i / 3));
			if (state.blockItem[i]) {
				stack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
				stack.scale(state.displayScale[i], state.displayScale[i], state.displayScale[i]);
				stack.translate(0.0F, state.displayOffsetY[i], 0.0F);
			} else {
				stack.scale(0.3F, 0.3F, 0.3F);
				stack.mulPose(Axis.YP.rotationDegrees(rotationAngle + 90.0F));
				stack.mulPose(Axis.XN.rotationDegrees(90.0F));
			}
			itemState.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
	}

	public static class DisplayerRenderState extends BlockEntityRenderState {
		public BlockState blockState = Blocks.AIR.defaultBlockState();
		public final ItemStackRenderState[] items = new ItemStackRenderState[SLOTS];
		public final boolean[] blockItem = new boolean[SLOTS];
		public final float[] displayScale = new float[SLOTS];
		public final float[] displayOffsetY = new float[SLOTS];

		public DisplayerRenderState() {
			for (int i = 0; i < SLOTS; i++) {
				this.items[i] = new ItemStackRenderState();
			}
		}
	}
}
