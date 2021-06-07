package com.red_x_tornado.assortedspells.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.red_x_tornado.assortedspells.BookOfAssortedSpells;
import com.red_x_tornado.assortedspells.capability.SpellCapability;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public abstract class Spell {

	private static final Map<ResourceLocation,Spell> ALL_SPELLS = new HashMap<>();

	public static final Spell SUMMON_KFC = new Spell(SpellClass.SUPPORT, SpellType.ARCANE, SpellDifficulty.SIMPLE, 3 * 20, 5 * 30, 30, builtin("summon_kfc"), ISpellCaster.DELAYED) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (!caps.getPlayer().world.isRemote) {
				final ChickenEntity chicken = new ChickenEntity(EntityType.CHICKEN, caps.getPlayer().world);
				chicken.setPositionAndRotation(ctx.getTarget().x, ctx.getTarget().y, ctx.getTarget().z, 0, 0);
				caps.getPlayer().world.addEntity(chicken);
			}
		}
		@Override
		public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {
			caps.getPlayer().world.addParticle(new RedstoneParticleData(1F, 1F, 1F, 1F), pos.x, pos.y, pos.z, 0, 0, 0);
		}
	};

	public static final Spell LIGHTNING = new Spell(SpellClass.ATTACK, SpellType.EARTH, SpellDifficulty.SIMPLE, 3 * 20, 5 * 30, 30, builtin("lightning"), ISpellCaster.INSTANT) {
		@Override
		protected void serverCast(SpellCapability caps, CastContext ctx) {
			if (!caps.getPlayer().world.isRemote) {
				final LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(caps.getPlayer().world);
				lightning.moveForced(ctx.getTarget());
				caps.getPlayer().world.addEntity(lightning);
			}
		}
		@Override
		public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {
			caps.getPlayer().world.addParticle(ParticleTypes.FIREWORK, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	};

	private final SpellClass clazz;
	private final SpellType type;
	private final SpellDifficulty difficulty;
	private final int baseCooldown;
	private final int baseDuration;
	private final int maxDistance;
	private final ISpellCaster caster;

	private final ResourceLocation id;
	private final String langKey;

	public Spell(SpellClass clazz, SpellType type, SpellDifficulty difficulty, int baseCooldown, int baseDuration, int maxDistance, ResourceLocation id, ISpellCaster caster) {
		this.clazz = clazz;
		this.type = type;
		this.difficulty = difficulty;
		this.baseCooldown = baseCooldown;
		this.baseDuration = baseDuration;
		this.maxDistance = maxDistance;
		this.id = id;
		this.caster = caster;
		langKey = "spell." + id.getNamespace() + "." + id.getPath();
		ALL_SPELLS.put(id, this);
	}

	private static ResourceLocation builtin(String path) {
		return new ResourceLocation(BookOfAssortedSpells.MOD_ID, path);
	}

	@Nullable
	public static Spell find(ResourceLocation id) {
		return ALL_SPELLS.get(id);
	}

	public static Set<ResourceLocation> getSpellIds() {
		return ALL_SPELLS.keySet();
	}

	public ISpellCaster getCaster() {
		return caster;
	}

	public void cast(SpellCapability caps, CastContext ctx) {
		if (caps.getPlayer().getEntityWorld().isRemote)
			clientCast(caps, ctx);
		else serverCast(caps, ctx);
	}

	protected abstract void serverCast(SpellCapability caps, CastContext ctx);

	protected void clientCast(SpellCapability caps, CastContext ctx) {}

	public void doBeamEffects(SpellCapability caps, CastContext ctx, Vector3d pos) {}

	public boolean prefersEntities() {
		return true;
	}

	public SpellClass getClazz() {
		return clazz;
	}

	public SpellType getType() {
		return type;
	}

	public SpellDifficulty getBaseDifficulty() {
		return difficulty;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public int getBaseDuration() {
		return baseDuration;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public ResourceLocation getId() {
		return id;
	}

	public String getLangKey() {
		return langKey;
	}
}