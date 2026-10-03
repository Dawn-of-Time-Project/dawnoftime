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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
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

	public static class DisplayerRenderState extends BlockEntityRenderState {
		public double xStart;
		public double yStart;
		public double zStart;
		public final ItemStackRenderState[] items = new ItemStackRenderState[SLOTS];
		public final boolean[] blockItems = new boolean[SLOTS];
		public final float[] blockScales = new float[SLOTS];
	}

	@Override
	public @NotNull DisplayerRenderState createRenderState() {
		return new DisplayerRenderState();
	}

	@Override
	public void extractRenderState(@NotNull DisplayerBlockEntity blockEntity, @NotNull DisplayerRenderState state, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);
		final BlockState blockState = blockEntity.getBlockState();
		final DisplayerBlock block = (DisplayerBlock) blockState.getBlock();
		state.xStart = block.getDisplayerX(blockState);
		state.yStart = block.getDisplayerY(blockState);
		state.zStart = block.getDisplayerZ(blockState);
		final int seed = (int) blockEntity.getBlockPos().asLong();
		for (int i = 0; i < SLOTS; i++) {
			final ItemStack itemStack = blockEntity.getItem(i);
			final ItemStackRenderState itemState = new ItemStackRenderState();
			final boolean isBlockItem = itemStack.getItem() instanceof BlockItem;
			if (!itemStack.isEmpty()) {
				this.itemModelResolver.updateForTopItem(itemState, itemStack, isBlockItem ? ItemDisplayContext.NONE : ItemDisplayContext.FIXED, blockEntity.getLevel(), null, i + seed);
			}
			state.items[i] = itemState;
			state.blockItems[i] = isBlockItem;
			state.blockScales[i] = -1.0F;
			if (isBlockItem) {
				final Block blockFromItem = ((BlockItem) itemStack.getItem()).getBlock();
				if (blockFromItem instanceof IBlockSpecialDisplay specialDisplay) {
					state.blockScales[i] = specialDisplay.getDisplayScale();
				}
			}
		}
	}

	@Override
	public void submit(@NotNull DisplayerRenderState state, @NotNull PoseStack stack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
		for (int i = 0; i < SLOTS; i++) {
			final ItemStackRenderState itemState = state.items[i];
			if (itemState.isEmpty()) {
				continue;
			}
			stack.pushPose();
			stack.translate(state.xStart, state.yStart, state.zStart);
			stack.translate((0.5D - state.xStart) * (i % 3), 0.015D, (0.5D - state.zStart) * Math.floor((double) i / 3));
			final float rotationAngle;
			if (i == 0 || i == 8) rotationAngle = 20.0F;
			else if (i == 2 || i == 6) rotationAngle = -20.0F;
			else rotationAngle = 0.0F;
			if (state.blockItems[i]) {
				stack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
				if (state.blockScales[i] >= 0.0F) {
					final float scale = state.blockScales[i];
					stack.scale(scale, scale, scale);
					stack.translate(0.0F, 0.485F, 0.0F);
				} else {
					stack.scale(0.2F, 0.2F, 0.2F);
					stack.translate(0.0F, 0.45F, 0.0F);
				}
			} else {
				stack.scale(0.3F, 0.3F, 0.3F);
				stack.mulPose(Axis.YP.rotationDegrees(rotationAngle + 90.0F));
				stack.mulPose(Axis.XN.rotationDegrees(90.0F));
			}
			itemState.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
	}
}
