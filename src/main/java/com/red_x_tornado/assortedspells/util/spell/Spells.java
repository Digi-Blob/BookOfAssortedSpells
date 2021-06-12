package com.red_x_tornado.assortedspells.util.spell;

import static com.red_x_tornado.assortedspells.util.ResourceLocations.as;
import static com.red_x_tornado.assortedspells.util.cast.ISpellCaster.*;
import static com.red_x_tornado.assortedspells.util.spell.SpellClass.*;
import static com.red_x_tornado.assortedspells.util.spell.SpellType.*;
import static com.red_x_tornado.assortedspells.util.spell.SpellDifficulty.*;

import com.red_x_tornado.assortedspells.capability.SpellCapability;
import com.red_x_tornado.assortedspells.util.cast.CastContext;
import com.red_x_tornado.assortedspells.util.cast.ISpellCaster;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * A class to hold the spells.
 */
public final class Spells {

	private Spells() {}

	/**
	 * A spell that places a chicken where you're looking.
	 */
	public static final Spell SUMMON_KFC = new ParticleBeamSpell(SUPPORT, ARCANE, SIMPLE, 3 * 20, 5 * 30, 30, as("summon_kfc"), DELAYED, new RedstoneParticleData(1F, 1F, 1F, 1F)) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, caps.getPlayer().world);
			caps.getPlayer().world.addEntity(ctx.moveToTarget(chicken));
		}
	};

	/**
	 * A spell that strikes the thing you're looking at with lightning.
	 */
	public static final Spell LIGHTNING = new ParticleBeamSpell(ATTACK, EARTH, SIMPLE, 3 * 20, 5 * 30, 30, as("lightning"), INSTANT, ParticleTypes.FIREWORK) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(caps.getPlayer().world);
			lightning.moveForced(ctx.getTarget());
			caps.getPlayer().world.addEntity(lightning);
		}
	};

	/**
	 * A spell that temporarily freezes an entity in place.
	 */
	public static final Spell FREEZE = new ParticleBeamSpell(ATTACK, WATER, SIMPLE, 5 * 20, 5 * 30, 30, as("freeze"), INSTANT, ParticleTypes.WHITE_ASH) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5 * 20, 100, false, false, true));
		}
	};

	/**
	 * A spell that messes with your redstone devices. It's like discount wireless redstone!
	 */
	public static final Spell POWER_REDSTONE = new ParticleBeamSpell(UTILITY, COSMIC, COMPLICATED, 20, 2 * 20, 30, as("power_redstone"), DELAYED, new RedstoneParticleData(1F, 0F, 0F, 1F)) {
		@SuppressWarnings("deprecation")
		@Override
		public void cast(SpellCapability caps, CastContext ctx) {
			final World world = caps.getPlayer().getEntityWorld();
			final BlockPos target = new BlockPos(ctx.getTarget());
			final BlockState state = world.getBlockState(target);
			if (state.hasProperty(BlockStateProperties.POWERED) && (state.getBlock() instanceof LeverBlock || state.getBlock() instanceof AbstractButtonBlock))
				state.getBlock().onBlockActivated(state, world, target, caps.getPlayer(), Hand.MAIN_HAND, new BlockRayTraceResult(ctx.getTarget(), Direction.UP, target, false));
		}
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {}
		@Override
		public boolean prefersEntities() {
			return false;
		}
	};

	/**
	 * A spell that launches entities!
	 */
	public static final Spell LAUNCH = new ParticleBeamSpell(ATTACK, AIR, EASY, 5 * 20, 2 * 20, 30, as("launch"), DELAYED, ParticleTypes.END_ROD) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final Entity e = ctx.getTargetEntity();
			if (e instanceof LivingEntity) {
				final int motion = 5;
				final Vector3d dir = ctx.getDirection();
				e.setMotion(e.getMotion().add(dir.x * motion, 2 + dir.y * motion, dir.z * motion));
			}
		}
	};

	private abstract static class ParticleBeamSpell extends Spell {

		private final IParticleData particle;

		public ParticleBeamSpell(SpellClass clazz, SpellType type, SpellDifficulty difficulty, int baseCooldown, int baseDuration, int maxDistance, ResourceLocation id, ISpellCaster caster, IParticleData particle) {
			super(clazz, type, difficulty, baseCooldown, baseDuration, maxDistance, id, caster);
			this.particle = particle;
		}

		@Override
		public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {
			caps.getPlayer().world.addParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}
}