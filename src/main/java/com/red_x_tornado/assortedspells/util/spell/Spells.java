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
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * A class to hold the spells.
 */
public final class Spells {

	private Spells() {}

	private static final int TICKS = 20;
	private static final int DIST = 30;

	/**
	 * A spell that places a chicken where you're looking.
	 */
	public static final Spell SUMMON_KFC = new ParticleBeamSpell(SUPPORT, ARCANE, SIMPLE, 3 * TICKS, 5 * DIST, DIST, as("summon_kfc"), DELAYED, new RedstoneParticleData(1F, 1F, 1F, 1F)) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, caps.getPlayer().world);
			caps.getPlayer().world.addEntity(ctx.moveToTarget(chicken));
		}
	};

	/**
	 * A spell that strikes the thing you're looking at with lightning.
	 */
	public static final Spell LIGHTNING = new ParticleBeamSpell(ATTACK, EARTH, SIMPLE, 3 * TICKS, 5 * DIST, DIST, as("lightning"), INSTANT, ParticleTypes.FIREWORK) {
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
	public static final Spell FREEZE = new ParticleBeamSpell(ATTACK, WATER, INSANE, 5 * TICKS, 5 * DIST, DIST, as("freeze"), INSTANT, ParticleTypes.WHITE_ASH) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5 * 20, 100, false, false, true));
		}
	};

	/**
	 * A spell that messes with your redstone devices. It's like discount wireless redstone!
	 */
	public static final Spell POWER_REDSTONE = new ParticleBeamSpell(UTILITY, COSMIC, COMPLICATED, TICKS, 2 * 20, DIST, as("power_redstone"), DELAYED, new RedstoneParticleData(1F, 0F, 0F, 1F)) {
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
	public static final Spell LAUNCH = new ParticleBeamSpell(ATTACK, AIR, EASY, 5 * TICKS, 2 * 20, DIST, as("launch"), DELAYED, ParticleTypes.END_ROD) {
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

	public static final Spell IGNITE = new ParticleBeamSpell(ATTACK, FIRE, EASY, 5 * TICKS, 2 * 20, DIST, as("ignite"), DELAYED, ParticleTypes.FLAME) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).setFire(10);
		}
	};

	public static final Spell TELEPORT = new ParticleBeamSpell(UTILITY, COSMIC, INSANE, 30 * TICKS, 5 * 20, DIST, as("teleport"), INSTANT, ParticleTypes.PORTAL) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final PlayerEntity player = caps.getPlayer();
			if (player.isPassenger()) {
				player.stopRiding();
			}

			player.setPositionAndUpdate(ctx.getTarget().x, ctx.getTarget().y, ctx.getTarget().z);
			player.fallDistance = 0F;
			player.world.playSound(null, ctx.getTarget().x, ctx.getTarget().y, ctx.getTarget().z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
		}
	};

	public static final Spell BREAK_BLOCK = new ParticleBeamSpell(UTILITY, EARTH, COMPLICATED, TICKS, 2 * 20, DIST, as("break_block"), INSTANT, new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.GRAVEL.getDefaultState())) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell PLACE_BLOCK = new ParticleBeamSpell(UTILITY, EARTH, EASY, TICKS, 2 * 20, DIST, as("place_block"), INSTANT, ParticleTypes.FIREWORK) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell MAGIC_MISSILE = new ParticleBeamSpell(ATTACK, ARCANE, SIMPLE, 2 * TICKS, 2 * 20, DIST, as("magic_missile"), DELAYED, ParticleTypes.FIREWORK) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).attackEntityFrom(DamageSource.GENERIC, 6);
		}
	};

	public static final Spell EXTINGUISH = new ParticleBeamSpell(SUPPORT, WATER, EASY, 5 * TICKS, 2 * 20, DIST, as("extinguish"), DELAYED, ParticleTypes.BUBBLE) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).extinguish();
		}
	};

	public static final Spell REGENERATE = new ParticleBeamSpell(SUPPORT, ARCANE, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("regenerate"), INSTANT, ParticleTypes.HEART) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.REGENERATION, 3 * 20, 100, false, false, true));
		}
	};

	public static final Spell DECAY = new ParticleBeamSpell(ATTACK, DARK, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("decay"), INSTANT, ParticleTypes.WITCH) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.WITHER, 3 * 20, 100, false, false, true));
		}
	};

	public static final Spell RAISE_THE_DEAD = new ParticleBeamSpell(ATTACK, DARK, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("raise_the_dead"), INSTANT, ParticleTypes.WITCH) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell RETURN = new ParticleBeamSpell(UTILITY, ARCANE, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("return"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell BIOLUMINESCENCE = new ParticleBeamSpell(SUPPORT, LIGHT, EASY, 10 * TICKS, 2 * 20, DIST, as("bioluminescence"), DELAYED, ParticleTypes.END_ROD) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.GLOWING, 30 * 20, 100, false, false, true));
		}
	};

	public static final Spell SATIATE = new ParticleBeamSpell(SUPPORT, ARCANE, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("satiate"), INSTANT, new RedstoneParticleData(32 / 255F, 24 / 255F, 19 / 255F, 1F)) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell WOLVES_EYE = new ParticleBeamSpell(UTILITY, ARCANE, EASY, 60 * TICKS, 2 * 20, DIST, as("wolves_eye"), INSTANT, ParticleTypes.END_ROD) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 60 * 20, 100, false, false, true));
		}
	};

	public static final Spell CONJURE_TEETH = new ParticleBeamSpell(ATTACK, DARK, INSANE, 5 * TICKS, 2 * 20, DIST, as("conjure_teeth"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			final PlayerEntity player = caps.getPlayer();
			final Entity target = ctx.getTargetEntity();
			final Vector3d vec = ctx.getTarget();

			double d0 = Math.min(vec.y, player.getPosY());
			double d1 = Math.max(vec.y, player.getPosY()) + 1D;
			float f = (float)MathHelper.atan2(vec.z - player.getPosZ(), vec.x - player.getPosX());
			if (target != null && player.getDistanceSq(target) < 9D) {
				for(int i = 0; i < 5; ++i) {
					float offset = f + i * (float)Math.PI * 0.4F;
					spawnFangs(player, player.getPosX() + MathHelper.cos(offset) * 1.5D, player.getPosZ() + MathHelper.sin(offset) * 1.5D, d0, d1, offset, 0);
				}

				for(int i = 0; i < 8; ++i) {
					float offset = f + i * (float)Math.PI * 2F / 8F + 1.2566371F;
					spawnFangs(player, player.getPosX() + MathHelper.cos(offset) * 2.5D, player.getPosZ() + MathHelper.sin(offset) * 2.5D, d0, d1, offset, 3);
				}
			} else {
				for(int i = 0; i < 16; ++i) {
					double offset = 1.25D * (i + 1);
					spawnFangs(player, player.getPosX() + MathHelper.cos(f) * offset, player.getPosZ() + MathHelper.sin(f) * offset, d0, d1, f, i);
				}
			}
		}

		private void spawnFangs(PlayerEntity player, double x, double z, double p_190876_5_, double y, float warmup, int rotationYaw) {
			BlockPos pos = new BlockPos(x, y, z);
			boolean addFang = false;
			double d0 = 0D;

			do {
				BlockPos downPos = pos.down();
				BlockState downState = player.world.getBlockState(downPos);
				if (downState.isSolidSide(player.world, downPos, Direction.UP)) {
					if (!player.world.isAirBlock(pos)) {
						BlockState state = player.world.getBlockState(pos);
						VoxelShape shape = state.getCollisionShapeUncached(player.world, pos);
						if (!shape.isEmpty()) {
							d0 = shape.getEnd(Direction.Axis.Y);
						}
					}

					addFang = true;
					break;
				}

				pos = pos.down();
			} while (pos.getY() >= MathHelper.floor(p_190876_5_) - 1);

			if (addFang) {
				player.world.addEntity(new EvokerFangsEntity(player.world, x, pos.getY() + d0, z, warmup, rotationYaw, player));
			}
		}
	};

	public static final Spell ROLL_DICE = new ParticleBeamSpell(UTILITY, ARCANE, SIMPLE, TICKS, 2 * 20, DIST, as("roll_dice"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			caps.getPlayer().sendMessage(new StringTextComponent("You rolled 1d6: " + (caps.getPlayer().getRNG().nextInt(6) + 1)), Util.DUMMY_UUID);
		}
	};

	public static final Spell GAMBLE = new ParticleBeamSpell(UTILITY, DARK, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("gamble"), DELAYED, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			
		}
	};

	public static final Spell CONJURE_DUST = new ParticleBeamSpell(UTILITY, EARTH, COMPLICATED, 5 * TICKS, 2 * 20, DIST, as("conjure_dust"), DELAYED, new RedstoneParticleData(32 / 255F, 24 / 255F, 19 / 255F, 1F)) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {}
	};

	public static final Spell MYSTIC_WRENCH = new ParticleBeamSpell(UTILITY, ARCANE, EASY, TICKS, 2 * 30, DIST, as("mystic_wrench"), INSTANT, ParticleTypes.END_ROD) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			BlockPos pos = new BlockPos(ctx.getTarget());
			if (caps.getPlayer().world.isAirBlock(pos))
				pos = pos.offset(ctx.getHitFace().getOpposite());

			final BlockState state = caps.getPlayer().world.getBlockState(pos);

			final BlockState newState = state.rotate(caps.getPlayer().world, pos, Rotation.CLOCKWISE_90);
			if (newState != state)
				caps.getPlayer().world.setBlockState(pos, newState);
		}
	};

	public static final Spell GROUND = new ParticleBeamSpell(ATTACK, EARTH, EASY, 30 * 20, 2 * TICKS, DIST, as("ground"), INSTANT, new RedstoneParticleData(32 / 255F, 24 / 255F, 19 / 255F, 1F)) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (ctx.getTargetEntity() instanceof LivingEntity)
				((LivingEntity) ctx.getTargetEntity()).addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 5 * 20, 255, false, false, true));
		}
	};

	/*public static final Spell LAUNCH_ARROW = new ParticleBeamSpell(ATTACK, AIR, SIMPLE, TICKS, 2 * 20, DIST, as("launch_arrow"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {}
	};*/

	public static final Spell CONJURE_STORM = new ParticleBeamSpell(UTILITY, AIR, COMPLICATED, 600 * TICKS, 2 * 20, DIST, as("conjure_storm"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			((ServerWorld) caps.getPlayer().world).setWeather(0, (caps.getPlayer().getRNG().nextInt(540) + 60) * 20, true, caps.getPlayer().getRNG().nextInt(3) == 0);
		}
	};

	/*public static final Spell RAIN_B_GONE = new ParticleBeamSpell(UTILITY, AIR, COMPLICATED, 600 * TICKS, 2 * 20, DIST, as("rain_b_gone"), INSTANT, null) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			((ServerWorld) caps.getPlayer().world).setWeather((caps.getPlayer().getRNG().nextInt(540) + 60) * 20, 0, false, false);
		}
	};*/

	private abstract static class ParticleBeamSpell extends Spell {

		private final IParticleData particle;

		public ParticleBeamSpell(SpellClass clazz, SpellType type, SpellDifficulty difficulty, int baseCooldown, int baseDuration, int maxDistance, ResourceLocation id, ISpellCaster caster, IParticleData particle) {
			super(clazz, type, difficulty, baseCooldown, baseDuration, maxDistance, id, caster);
			this.particle = particle;
		}

		@Override
		public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {
			if (particle != null)
				caps.getPlayer().world.addParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}
}