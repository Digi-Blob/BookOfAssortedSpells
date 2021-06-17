package com.red_x_tornado.assortedspells.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.item.WandItem;
import com.red_x_tornado.assortedspells.util.cast.CastContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//@EventBusSubscriber(modid = BookOfAssortedSpells.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.FORGE)
public class CastingDebugRenderer {

	private static final boolean ENABLED = false;

	@SubscribeEvent
	public static void renderLast(RenderWorldLastEvent event) {
		if (!ENABLED) return;
		final Minecraft mc = Minecraft.getInstance();
		final ItemStack wand = mc.player.getHeldItemMainhand();

		if (wand.getItem() instanceof WandItem) {
			final IRenderTypeBuffer.Impl bufferIn = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
			final IVertexBuilder lines = bufferIn.getBuffer(RenderType.getLines());
			final MatrixStack matrixStack = event.getMatrixStack();

			final Vector3d vec = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

			matrixStack.push();
			matrixStack.translate(-vec.x, -vec.y, -vec.z); // Translate back to world.

			renderCastRay(matrixStack, lines, mc.player, wand);

			matrixStack.pop();

			bufferIn.finish(RenderType.getLines());
		}
	}

	private static void renderBoundingBox(MatrixStack matrixStack, IVertexBuilder lines, Vector3d pos, float r, float g, float b) {
		WorldRenderer.drawBoundingBox(matrixStack, lines, pos.getX() - 0.1, pos.getY() - 0.1, pos.getZ() - 0.1, pos.getX() + 0.1, pos.getY() + 0.1, pos.getZ() + 0.1, r, g, b, 1F);
	}

	private static void renderCastRay(MatrixStack matrixStack, IVertexBuilder lines, PlayerEntity player, ItemStack wand) {
		final SpellCapability caps = SpellCapability.get(player);
		if (caps.getSelected() != null) {
			final int distance = caps.getSelected().getSpell().getMaxDistance();
			final Vector3d start = player.getEyePosition(0F);
			final Vector3d look = player.getLook(0F);

			Vector3d end = new Vector3d(start.x + look.x * distance, start.y + look.y * distance, start.z + look.z * distance);

			final RayTraceResult ray = player.pick(distance, 0F, false);
			end = ray.getHitVec();

			// Render block hit.
			renderBoundingBox(matrixStack, lines, end, 0F, 0F, 1F);

			final ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, player.world);
			chicken.moveForced(end);

			final Direction hitFace = ray instanceof BlockRayTraceResult ? ((BlockRayTraceResult) ray).getFace() : null;

			final Vector3d corrected = CastContext.correct(chicken, end, hitFace);
			renderBoundingBox(matrixStack, lines, corrected, 0F, 1F, 0F);

			// Render entity trace bounds.
			//WorldRenderer.drawBoundingBox(matrixStack, lines, new AxisAlignedBB(start, end), 1F, 0F, 0F, 1F);

			final EntityRayTraceResult entityRay = caps.getSelected().getSpell().prefersEntities() ? ProjectileHelper.rayTraceEntities(player.world, player, start, end, new AxisAlignedBB(start, end), null) : null;

			final Entity targetEntity = entityRay == null ? null : entityRay.getEntity();

			if (targetEntity != null) {
				final Vector3d newHit = targetEntity.getBoundingBox().rayTrace(start, end).orElse(null);

				if (newHit != null)
					renderBoundingBox(matrixStack, lines, newHit, 0F, 1F, 1F);
			}
		}
	}
}